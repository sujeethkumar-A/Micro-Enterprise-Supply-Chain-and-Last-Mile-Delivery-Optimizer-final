package com.example.supplychain.config;

import com.example.supplychain.enums.DeliveryStatus;
import com.example.supplychain.enums.OrderStatus;
import com.example.supplychain.model.*;
import com.example.supplychain.repository.*;
import com.example.supplychain.service.RouteOptimizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final DeliveryRepository deliveryRepository;
    private final RouteOptimizationService routeOptimizationService;

    public DataInitializer(SupplierRepository supplierRepository,
                           ProductRepository productRepository,
                           CustomerRepository customerRepository,
                           OrderRepository orderRepository,
                           OrderItemRepository orderItemRepository,
                           DeliveryRepository deliveryRepository,
                           RouteOptimizationService routeOptimizationService) {
        this.supplierRepository = supplierRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.deliveryRepository = deliveryRepository;
        this.routeOptimizationService = routeOptimizationService;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (supplierRepository.count() > 0) {
            log.info("Sample data already present. Skipping database initialization.");
            return;
        }

        log.info("Seeding initial sample data for Micro-Enterprise Supply Chain system...");

        // 1. Seed 3 Suppliers
        Supplier s1 = new Supplier("Apex Packaging Solutions", "Rajesh Sharma", "+91 98450 11223", "apex.supplies@gmail.com", "Plot 14, Peenya Industrial Area, Bengaluru");
        Supplier s2 = new Supplier("ElectroCraft Components", "Priya Nair", "+91 98860 33445", "sales@electrocraft.in", "45 Electronics City Phase 1, Bengaluru");
        Supplier s3 = new Supplier("Standard Stationery Hub", "Anand Verma", "+91 97410 55667", "orders@standardstationery.com", "88 Chickpet Commercial Cross, Bengaluru");

        s1 = supplierRepository.save(s1);
        s2 = supplierRepository.save(s2);
        s3 = supplierRepository.save(s3);

        // 2. Seed 8 Products (2 with low stock for dashboard alerting)
        Product p1 = new Product("Corrugated Shipping Box (Large)", "Packaging", 45.0, 50, 20, "Durable 5-ply heavy duty shipping boxes", s1);
        Product p2 = new Product("Bubble Wrap Roll (50m)", "Packaging", 280.0, 3, 10, "Air cushion packaging roll for fragile items", s1); // LOW STOCK
        Product p3 = new Product("Self-Adhesive Packing Tape", "Packaging", 65.0, 120, 25, "Standard brown parcel packing tape (65m)", s1);

        Product p4 = new Product("Digital Weighing Scale (30kg)", "Electronics", 1850.0, 15, 5, "Precise postal dispatch parcel scale", s2);
        Product p5 = new Product("Handheld Barcode Scanner", "Electronics", 1250.0, 8, 4, "USB plug-and-play wireless laser scanner", s2);
        Product p6 = new Product("Thermal Receipt Paper Roll (Pack of 10)", "Stationery", 220.0, 4, 5, "BPA-free high sensitivity thermal paper rolls", s3); // LOW STOCK

        Product p7 = new Product("Heavy Duty Stapler & Pin Pack", "Stationery", 340.0, 30, 8, "Manual desktop carton and paper stapler", s3);
        Product p8 = new Product("Permanent Marking Pens (Pack of 12)", "Stationery", 180.0, 45, 15, "Waterproof black chisel tip shipping markers", s3);

        p1 = productRepository.save(p1);
        p2 = productRepository.save(p2);
        p3 = productRepository.save(p3);
        p4 = productRepository.save(p4);
        p5 = productRepository.save(p5);
        p6 = productRepository.save(p6);
        p7 = productRepository.save(p7);
        p8 = productRepository.save(p8);

        // 3. Seed 5 Customers located in Bengaluru coordinates
        // Depot is at (12.9716, 77.5946) Central Bangalore
        Customer c1 = new Customer("Ramesh Kumar (Retailer)", "+91 91234 56789", "ramesh.stores@gmail.com", "Shop 12, Shanti Nagar Main Road", "Bengaluru", 12.9560, 77.5950);
        Customer c2 = new Customer("Kiran Deep (Supermarket)", "+91 92345 67890", "kiran.supermart@outlook.com", "23 7th Block, Koramangala", "Bengaluru", 12.9352, 77.6245);
        Customer c3 = new Customer("Sunil Gowda (Hardware)", "+91 93456 78901", "sunil.hardware@yahoo.com", "102 27th Main, Sector 1, HSR Layout", "Bengaluru", 12.9121, 77.6446);
        Customer c4 = new Customer("Sneha Patel (Bookstore)", "+91 94567 89012", "sneha.books@gmail.com", "55 100ft Road, Indiranagar", "Bengaluru", 12.9784, 77.6408);
        Customer c5 = new Customer("Vikram Rao (IT Mart)", "+91 95678 90123", "vikram.it@techmail.com", "8 ITPL Main Road, Whitefield", "Bengaluru", 12.9698, 77.7499);

        c1 = customerRepository.save(c1);
        c2 = customerRepository.save(c2);
        c3 = customerRepository.save(c3);
        c4 = customerRepository.save(c4);
        c5 = customerRepository.save(c5);

        // 4. Seed 5 Orders
        // Order 1 (Placed) - Customer 1
        CustomerOrder o1 = new CustomerOrder(c1, LocalDateTime.now().minusHours(5), 450.0, OrderStatus.PLACED);
        o1 = orderRepository.save(o1);
        OrderItem oi1_1 = new OrderItem(o1, p1, 10, p1.getPrice());
        orderItemRepository.save(oi1_1);
        o1.getItems().add(oi1_1);

        // Order 2 (Placed) - Customer 2
        CustomerOrder o2 = new CustomerOrder(c2, LocalDateTime.now().minusHours(4), 1850.0, OrderStatus.PLACED);
        o2 = orderRepository.save(o2);
        OrderItem oi2_1 = new OrderItem(o2, p4, 1, p4.getPrice());
        orderItemRepository.save(oi2_1);
        o2.getItems().add(oi2_1);

        // Order 3 (Placed) - Customer 3
        CustomerOrder o3 = new CustomerOrder(c3, LocalDateTime.now().minusHours(3), 1250.0, OrderStatus.PLACED);
        o3 = orderRepository.save(o3);
        OrderItem oi3_1 = new OrderItem(o3, p5, 1, p5.getPrice());
        orderItemRepository.save(oi3_1);
        o3.getItems().add(oi3_1);

        // Order 4 (Delivered) - Customer 4
        CustomerOrder o4 = new CustomerOrder(c4, LocalDateTime.now().minusDays(1), 540.0, OrderStatus.DELIVERED);
        o4 = orderRepository.save(o4);
        OrderItem oi4_1 = new OrderItem(o4, p7, 1, p7.getPrice());
        OrderItem oi4_2 = new OrderItem(o4, p8, 1, p8.getPrice());
        orderItemRepository.save(oi4_1);
        orderItemRepository.save(oi4_2);
        o4.getItems().add(oi4_1);
        o4.getItems().add(oi4_2);

        // Order 5 (Confirmed) - Customer 5
        CustomerOrder o5 = new CustomerOrder(c5, LocalDateTime.now().minusHours(1), 650.0, OrderStatus.CONFIRMED);
        o5 = orderRepository.save(o5);
        OrderItem oi5_1 = new OrderItem(o5, p3, 10, p3.getPrice());
        orderItemRepository.save(oi5_1);
        o5.getItems().add(oi5_1);

        // 5. Seed Deliveries (3 Pending for Route Optimization Demo + 1 Delivered)
        createDeliveryRecord(o1, DeliveryStatus.PENDING, c1);
        createDeliveryRecord(o2, DeliveryStatus.PENDING, c2);
        createDeliveryRecord(o3, DeliveryStatus.PENDING, c3);
        createDeliveryRecord(o4, DeliveryStatus.DELIVERED, c4);

        log.info("Sample data initialization completed successfully.");
    }

    private void createDeliveryRecord(CustomerOrder order, DeliveryStatus status, Customer customer) {
        double dist = routeOptimizationService.calculateHaversineDistance(
                routeOptimizationService.getDefaultDepotLat(),
                routeOptimizationService.getDefaultDepotLon(),
                customer.getLatitude(),
                customer.getLongitude()
        );
        double cost = routeOptimizationService.calculateDeliveryCost(dist);
        double time = routeOptimizationService.calculateDeliveryTimeMinutes(dist);

        Delivery delivery = new Delivery(order, LocalDateTime.now().plusDays(1), status, dist, cost, time);
        deliveryRepository.save(delivery);
    }
}
