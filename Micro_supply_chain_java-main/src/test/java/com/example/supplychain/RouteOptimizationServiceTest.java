package com.example.supplychain;

import com.example.supplychain.dto.RouteOptimizationRequest;
import com.example.supplychain.dto.RouteOptimizationResponse;
import com.example.supplychain.enums.DeliveryStatus;
import com.example.supplychain.enums.OrderStatus;
import com.example.supplychain.model.Customer;
import com.example.supplychain.model.CustomerOrder;
import com.example.supplychain.model.Delivery;
import com.example.supplychain.repository.DeliveryRepository;
import com.example.supplychain.service.RouteOptimizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class RouteOptimizationServiceTest {

    private DeliveryRepository deliveryRepository;
    private RouteOptimizationService routeService;

    @BeforeEach
    void setUp() {
        deliveryRepository = Mockito.mock(DeliveryRepository.class);
        routeService = new RouteOptimizationService(deliveryRepository);

        ReflectionTestUtils.setField(routeService, "baseDeliveryCost", 30.0);
        ReflectionTestUtils.setField(routeService, "costPerKm", 8.0);
        ReflectionTestUtils.setField(routeService, "averageSpeedKmh", 30.0);
        ReflectionTestUtils.setField(routeService, "defaultDepotName", "Central Warehouse Depot");
        ReflectionTestUtils.setField(routeService, "defaultDepotLat", 12.9716);
        ReflectionTestUtils.setField(routeService, "defaultDepotLon", 77.5946);
    }

    @Test
    @DisplayName("Verify Nearest-Neighbour Sequencing: Depot -> Closest Customer A -> Customer B")
    void testNearestNeighbourRouteOptimization() {
        // Depot is at (12.9716, 77.5946)
        // Customer 1: Shanti Nagar (12.9560, 77.5950) -> ~1.73 km from Depot
        Customer c1 = new Customer("Customer A (Near)", "111", "a@test.com", "Road 1", "City", 12.9560, 77.5950);
        CustomerOrder o1 = new CustomerOrder(c1, LocalDateTime.now(), 500.0, OrderStatus.PLACED);
        o1.setId(1L);
        Delivery d1 = new Delivery(o1, LocalDateTime.now(), DeliveryStatus.PENDING, 1.73, 43.84, 3.5);
        d1.setId(101L);

        // Customer 2: HSR Layout (12.9121, 77.6446) -> ~8.5 km from Depot
        Customer c2 = new Customer("Customer B (Far)", "222", "b@test.com", "Road 2", "City", 12.9121, 77.6446);
        CustomerOrder o2 = new CustomerOrder(c2, LocalDateTime.now(), 800.0, OrderStatus.PLACED);
        o2.setId(2L);
        Delivery d2 = new Delivery(o2, LocalDateTime.now(), DeliveryStatus.PENDING, 8.5, 98.0, 17.0);
        d2.setId(102L);

        // Pass deliveries in reverse order [Far, Near]
        List<Long> deliveryIds = Arrays.asList(102L, 101L);
        when(deliveryRepository.findAllById(deliveryIds)).thenReturn(Arrays.asList(d2, d1));

        RouteOptimizationRequest request = new RouteOptimizationRequest();
        request.setDeliveryIds(deliveryIds);

        RouteOptimizationResponse response = routeService.optimizeRoute(request);

        assertNotNull(response);
        assertEquals(2, response.getTotalStops());
        assertTrue(response.getTotalDistanceKm() > 0);
        assertTrue(response.getTotalEstimatedCost() > 30.0);

        // Stops should include Stop 0 (Depot), Stop 1 (Customer A - closest), Stop 2 (Customer B)
        assertEquals(3, response.getStops().size());
        assertEquals("DEPOT", response.getStops().get(0).getStopType());
        
        // Stop 1 MUST be Customer A because it is closest to Depot!
        assertEquals("Customer A (Near)", response.getStops().get(1).getCustomerName());
        assertEquals("Customer B (Far)", response.getStops().get(2).getCustomerName());
    }
}
