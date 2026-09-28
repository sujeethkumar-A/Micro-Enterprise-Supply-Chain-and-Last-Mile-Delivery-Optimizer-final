package com.example.supplychain;

import com.example.supplychain.repository.DeliveryRepository;
import com.example.supplychain.service.RouteOptimizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DistanceCalculationTest {

    private RouteOptimizationService routeService;

    @BeforeEach
    void setUp() {
        DeliveryRepository deliveryRepository = Mockito.mock(DeliveryRepository.class);
        routeService = new RouteOptimizationService(deliveryRepository);

        // Inject configuration values for delivery pricing and speed
        ReflectionTestUtils.setField(routeService, "baseDeliveryCost", 30.0);
        ReflectionTestUtils.setField(routeService, "costPerKm", 8.0);
        ReflectionTestUtils.setField(routeService, "averageSpeedKmh", 30.0);
        ReflectionTestUtils.setField(routeService, "defaultDepotName", "Central Depot");
        ReflectionTestUtils.setField(routeService, "defaultDepotLat", 12.9716);
        ReflectionTestUtils.setField(routeService, "defaultDepotLon", 77.5946);
    }

    @Test
    @DisplayName("Verify Haversine Great-Circle Distance Calculation between Bangalore points")
    void testHaversineDistanceCalculation() {
        // Point 1: Bangalore CBD (12.9716, 77.5946)
        // Point 2: Shanti Nagar (12.9560, 77.5950)
        double distance = routeService.calculateHaversineDistance(12.9716, 77.5946, 12.9560, 77.5950);

        // Distance is ~1.73 km
        assertTrue(distance > 1.5 && distance < 2.0, "Expected distance between 1.5km and 2.0km, got: " + distance);
        assertEquals(1.73, distance, 0.05);
    }

    @Test
    @DisplayName("Verify Delivery Cost: Base ₹30 + (5 km * ₹8/km) = ₹70")
    void testDeliveryCostCalculation() {
        double distance = 5.0; // km
        double cost = routeService.calculateDeliveryCost(distance);

        // 30 + (5 * 8) = ₹70.0
        assertEquals(70.0, cost, 0.01);
    }

    @Test
    @DisplayName("Verify Delivery Time: 15 km at 30 km/h = 30 minutes")
    void testDeliveryTimeCalculation() {
        double distance = 15.0; // km
        double timeMinutes = routeService.calculateDeliveryTimeMinutes(distance);

        // (15 / 30) * 60 = 30 minutes
        assertEquals(30.0, timeMinutes, 0.1);
    }
}
