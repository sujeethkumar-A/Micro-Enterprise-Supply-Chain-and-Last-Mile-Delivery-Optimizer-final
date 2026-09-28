package com.example.supplychain.service;

import com.example.supplychain.dto.DeliveryDTO;
import com.example.supplychain.enums.DeliveryStatus;
import com.example.supplychain.enums.OrderStatus;
import com.example.supplychain.exception.ResourceNotFoundException;
import com.example.supplychain.model.Customer;
import com.example.supplychain.model.CustomerOrder;
import com.example.supplychain.model.Delivery;
import com.example.supplychain.repository.DeliveryRepository;
import com.example.supplychain.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;
    private final RouteOptimizationService routeOptimizationService;

    public DeliveryService(DeliveryRepository deliveryRepository,
                           OrderRepository orderRepository,
                           RouteOptimizationService routeOptimizationService) {
        this.deliveryRepository = deliveryRepository;
        this.orderRepository = orderRepository;
        this.routeOptimizationService = routeOptimizationService;
    }

    /**
     * Initializes delivery tracking for an order, computing direct depot distance,
     * base cost, and initial estimated transit time.
     */
    public Delivery createDeliveryForOrder(CustomerOrder order) {
        Optional<Delivery> existing = deliveryRepository.findByOrderId(order.getId());
        if (existing.isPresent()) {
            return existing.get();
        }

        Customer customer = order.getCustomer();
        double distance = 0.0;
        double cost = routeOptimizationService.getBaseDeliveryCost();
        double time = 0.0;

        if (customer != null && customer.getLatitude() != null && customer.getLongitude() != null) {
            distance = routeOptimizationService.calculateHaversineDistance(
                    routeOptimizationService.getDefaultDepotLat(),
                    routeOptimizationService.getDefaultDepotLon(),
                    customer.getLatitude(),
                    customer.getLongitude()
            );
            cost = routeOptimizationService.calculateDeliveryCost(distance);
            time = routeOptimizationService.calculateDeliveryTimeMinutes(distance);
        }

        Delivery delivery = new Delivery(
                order,
                LocalDateTime.now().plusDays(1),
                DeliveryStatus.PENDING,
                distance,
                cost,
                time
        );

        return deliveryRepository.save(delivery);
    }

    public Delivery updateDeliveryStatus(Long deliveryId, DeliveryStatus newStatus) {
        Delivery delivery = getDeliveryById(deliveryId);
        delivery.setStatus(newStatus);

        // If delivered, update order status to DELIVERED as well
        if (newStatus == DeliveryStatus.DELIVERED && delivery.getOrder() != null) {
            CustomerOrder order = delivery.getOrder();
            order.setStatus(OrderStatus.DELIVERED);
            orderRepository.save(order);
        }

        return deliveryRepository.save(delivery);
    }

    @Transactional(readOnly = true)
    public Delivery getDeliveryById(Long id) {
        return deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found with ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Transactional(readOnly = true)
    public Page<Delivery> getDeliveriesPaged(int page, int size, DeliveryStatus status) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        if (status != null) {
            return deliveryRepository.findByStatus(status, pageable);
        }
        return deliveryRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<Delivery> getPendingDeliveries() {
        return deliveryRepository.findByStatus(DeliveryStatus.PENDING);
    }

    @Transactional(readOnly = true)
    public List<Delivery> getRecentDeliveries() {
        return deliveryRepository.findTop5ByOrderByDeliveryDateDesc();
    }

    @Transactional(readOnly = true)
    public long countDeliveries() {
        return deliveryRepository.count();
    }

    @Transactional(readOnly = true)
    public long countPendingDeliveries() {
        return deliveryRepository.countByStatus(DeliveryStatus.PENDING) + deliveryRepository.countByStatus(DeliveryStatus.ASSIGNED);
    }

    @Transactional(readOnly = true)
    public long countCompletedDeliveries() {
        return deliveryRepository.countByStatus(DeliveryStatus.DELIVERED);
    }

    public DeliveryDTO toDTO(Delivery delivery) {
        DeliveryDTO dto = new DeliveryDTO();
        dto.setId(delivery.getId());
        dto.setDeliveryDate(delivery.getDeliveryDate());
        dto.setStatus(delivery.getStatus());
        dto.setDistance(delivery.getDistance());
        dto.setEstimatedCost(delivery.getEstimatedCost());
        dto.setEstimatedTime(delivery.getEstimatedTime());

        if (delivery.getOrder() != null) {
            dto.setOrderId(delivery.getOrder().getId());
            Customer customer = delivery.getOrder().getCustomer();
            if (customer != null) {
                dto.setCustomerId(customer.getId());
                dto.setCustomerName(customer.getName());
                dto.setCustomerPhone(customer.getPhone());
                dto.setCustomerAddress(customer.getAddress());
                dto.setCustomerCity(customer.getCity());
                dto.setCustomerLatitude(customer.getLatitude());
                dto.setCustomerLongitude(customer.getLongitude());
            }
        }
        return dto;
    }
}
