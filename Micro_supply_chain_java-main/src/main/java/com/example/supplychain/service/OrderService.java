package com.example.supplychain.service;

import com.example.supplychain.dao.OrderDAO;
import com.example.supplychain.dao.ProductDAO;
import com.example.supplychain.dto.*;
import com.example.supplychain.enums.OrderStatus;
import com.example.supplychain.exception.InsufficientStockException;
import com.example.supplychain.exception.ResourceNotFoundException;
import com.example.supplychain.model.Customer;
import com.example.supplychain.model.CustomerOrder;
import com.example.supplychain.model.OrderItem;
import com.example.supplychain.model.Product;
import com.example.supplychain.repository.OrderRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDAO orderDAO;
    private final ProductDAO productDAO;
    private final CustomerService customerService;
    private final DeliveryService deliveryService;

    public OrderService(OrderRepository orderRepository,
                        OrderDAO orderDAO,
                        ProductDAO productDAO,
                        CustomerService customerService,
                        @Lazy DeliveryService deliveryService) {
        this.orderRepository = orderRepository;
        this.orderDAO = orderDAO;
        this.productDAO = productDAO;
        this.customerService = customerService;
        this.deliveryService = deliveryService;
    }

    /**
     * Creates an order with strict stock validation, calculates totals,
     * reduces inventory, and initializes delivery tracking.
     */
    public CustomerOrder createOrder(OrderCreateRequest request) {
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order must have at least one product item.");
        }

        // 1. Find Customer
        Customer customer = customerService.getCustomerById(request.getCustomerId());

        CustomerOrder order = new CustomerOrder();
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PLACED);

        double totalAmount = 0.0;
        List<OrderItem> orderItems = new ArrayList<>();

        // 2-5. Validate stock and calculate item totals
        for (OrderItemRequest itemReq : request.getItems()) {
            if (itemReq.getProductId() == null || itemReq.getQuantity() == null || itemReq.getQuantity() <= 0) {
                continue;
            }

            Product product = productDAO.findById(itemReq.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + itemReq.getProductId()));

            int requestedQty = itemReq.getQuantity();
            int availableQty = product.getStockQuantity();

            // Strict Stock Check
            if (availableQty < requestedQty) {
                throw new InsufficientStockException("Insufficient stock for product '" + product.getName() +
                        "'. Requested: " + requestedQty + ", Available: " + availableQty);
            }

            // Deduct stock
            product.setStockQuantity(availableQty - requestedQty);
            productDAO.save(product);

            // Create Order Item
            OrderItem orderItem = new OrderItem(order, product, requestedQty, product.getPrice());
            orderItems.add(orderItem);

            totalAmount += product.getPrice() * requestedQty;
        }

        if (orderItems.isEmpty()) {
            throw new IllegalArgumentException("No valid items provided for order.");
        }

        order.setTotalAmount(Math.round(totalAmount * 100.0) / 100.0);
        order.setItems(orderItems);

        // Save order and items (CascadeType.ALL saves items)
        CustomerOrder savedOrder = orderDAO.save(order);

        // Auto-create delivery tracking for this order
        try {
            deliveryService.createDeliveryForOrder(savedOrder);
        } catch (Exception e) {
            // Log but don't fail order creation
        }

        return savedOrder;
    }

    public CustomerOrder updateOrderStatus(Long orderId, OrderStatus newStatus) {
        CustomerOrder order = getOrderById(orderId);
        OrderStatus oldStatus = order.getStatus();

        if (oldStatus == newStatus) {
            return order;
        }

        // If order is cancelled, restore stock!
        if (newStatus == OrderStatus.CANCELLED && oldStatus != OrderStatus.CANCELLED) {
            for (OrderItem item : order.getItems()) {
                Product product = item.getProduct();
                product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
                productDAO.save(product);
            }
        }

        order.setStatus(newStatus);
        return orderDAO.save(order);
    }

    @Transactional(readOnly = true)
    public CustomerOrder getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<CustomerOrder> getAllOrders() {
        return orderDAO.findAll();
    }

    @Transactional(readOnly = true)
    public Page<CustomerOrder> getOrdersPaged(int page, int size, OrderStatus status) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "orderDate"));
        if (status != null) {
            return orderRepository.findByStatus(status, pageable);
        }
        return orderRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<CustomerOrder> getRecentOrders() {
        return orderDAO.findRecentOrders();
    }

    @Transactional(readOnly = true)
    public long countOrders() {
        return orderDAO.count();
    }

    @Transactional(readOnly = true)
    public long countPendingOrders() {
        return orderDAO.countByStatus(OrderStatus.PLACED) + orderDAO.countByStatus(OrderStatus.CONFIRMED);
    }

    public OrderDTO toDTO(CustomerOrder order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setOrderDate(order.getOrderDate());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus());

        if (order.getCustomer() != null) {
            dto.setCustomerId(order.getCustomer().getId());
            dto.setCustomerName(order.getCustomer().getName());
            dto.setCustomerPhone(order.getCustomer().getPhone());
            dto.setCustomerAddress(order.getCustomer().getAddress());
            dto.setCustomerCity(order.getCustomer().getCity());
            dto.setCustomerLatitude(order.getCustomer().getLatitude());
            dto.setCustomerLongitude(order.getCustomer().getLongitude());
        }

        List<OrderItemDTO> itemDTOs = new ArrayList<>();
        if (order.getItems() != null) {
            for (OrderItem item : order.getItems()) {
                OrderItemDTO itemDTO = new OrderItemDTO();
                itemDTO.setId(item.getId());
                if (item.getProduct() != null) {
                    itemDTO.setProductId(item.getProduct().getId());
                    itemDTO.setProductName(item.getProduct().getName());
                    itemDTO.setProductCategory(item.getProduct().getCategory());
                }
                itemDTO.setQuantity(item.getQuantity());
                itemDTO.setPrice(item.getPrice());
                itemDTO.setSubtotal(item.getSubtotal());
                itemDTOs.add(itemDTO);
            }
        }
        dto.setItems(itemDTOs);
        return dto;
    }
}
