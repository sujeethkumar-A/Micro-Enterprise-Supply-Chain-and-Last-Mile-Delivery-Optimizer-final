package com.example.supplychain;

import com.example.supplychain.dao.OrderDAO;
import com.example.supplychain.dao.ProductDAO;
import com.example.supplychain.dto.OrderCreateRequest;
import com.example.supplychain.dto.OrderItemRequest;
import com.example.supplychain.enums.OrderStatus;
import com.example.supplychain.exception.InsufficientStockException;
import com.example.supplychain.model.Customer;
import com.example.supplychain.model.CustomerOrder;
import com.example.supplychain.model.Product;
import com.example.supplychain.repository.OrderRepository;
import com.example.supplychain.service.CustomerService;
import com.example.supplychain.service.DeliveryService;
import com.example.supplychain.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    private OrderRepository orderRepository;
    private OrderDAO orderDAO;
    private ProductDAO productDAO;
    private CustomerService customerService;
    private DeliveryService deliveryService;
    private OrderService orderService;

    private Customer sampleCustomer;
    private Product sampleProduct;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        orderDAO = mock(OrderDAO.class);
        productDAO = mock(ProductDAO.class);
        customerService = mock(CustomerService.class);
        deliveryService = mock(DeliveryService.class);

        orderService = new OrderService(orderRepository, orderDAO, productDAO, customerService, deliveryService);

        sampleCustomer = new Customer("John Retail", "+91 98888 77777", "john@retail.com", "Main Road", "Bengaluru", 12.95, 77.59);
        sampleCustomer.setId(1L);

        sampleProduct = new Product("Tape", "Packaging", 50.0, 20, 5, "Packing Tape", null);
        sampleProduct.setId(10L);

        when(customerService.getCustomerById(1L)).thenReturn(sampleCustomer);
        when(productDAO.findById(10L)).thenReturn(Optional.of(sampleProduct));
        when(orderDAO.save(any(CustomerOrder.class))).thenAnswer(invocation -> {
            CustomerOrder o = invocation.getArgument(0);
            o.setId(100L);
            return o;
        });
    }

    @Test
    @DisplayName("Verify Order Creation: Stock deducted and total calculated accurately")
    void testOrderCreationSuccess() {
        OrderCreateRequest request = new OrderCreateRequest();
        request.setCustomerId(1L);
        request.setItems(Collections.singletonList(new OrderItemRequest(10L, 5)));

        CustomerOrder order = orderService.createOrder(request);

        assertNotNull(order);
        assertEquals(100L, order.getId());
        assertEquals(OrderStatus.PLACED, order.getStatus());
        assertEquals(250.0, order.getTotalAmount(), 0.01); // 5 * 50 = 250
        assertEquals(15, sampleProduct.getStockQuantity()); // 20 - 5 = 15

        verify(productDAO).save(sampleProduct);
        verify(orderDAO).save(any(CustomerOrder.class));
    }

    @Test
    @DisplayName("Verify Insufficient Stock: Throws InsufficientStockException and does not save order")
    void testOrderInsufficientStockThrowsException() {
        OrderCreateRequest request = new OrderCreateRequest();
        request.setCustomerId(1L);
        // Request 30 when available is 20
        request.setItems(Collections.singletonList(new OrderItemRequest(10L, 30)));

        InsufficientStockException exception = assertThrows(InsufficientStockException.class, () -> {
            orderService.createOrder(request);
        });

        assertTrue(exception.getMessage().contains("Insufficient stock"));
        assertEquals(20, sampleProduct.getStockQuantity(), "Stock should not be altered on failure");
        verify(orderDAO, never()).save(any(CustomerOrder.class));
    }
}
