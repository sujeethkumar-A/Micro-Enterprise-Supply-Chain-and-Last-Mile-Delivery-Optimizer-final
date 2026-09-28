package com.example.supplychain.controller;

import com.example.supplychain.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final SupplierService supplierService;
    private final ProductService productService;
    private final CustomerService customerService;
    private final OrderService orderService;
    private final DeliveryService deliveryService;

    public DashboardController(SupplierService supplierService,
                               ProductService productService,
                               CustomerService customerService,
                               OrderService orderService,
                               DeliveryService deliveryService) {
        this.supplierService = supplierService;
        this.productService = productService;
        this.customerService = customerService;
        this.orderService = orderService;
        this.deliveryService = deliveryService;
    }

    @GetMapping({"/", "/dashboard"})
    public String showDashboard(Model model) {
        // Metric Counters directly from Database
        model.addAttribute("totalSuppliers", supplierService.countSuppliers());
        model.addAttribute("totalProducts", productService.countProducts());
        model.addAttribute("totalCustomers", customerService.countCustomers());
        model.addAttribute("totalOrders", orderService.countOrders());
        model.addAttribute("pendingOrders", orderService.countPendingOrders());
        model.addAttribute("pendingDeliveries", deliveryService.countPendingDeliveries());
        model.addAttribute("completedDeliveries", deliveryService.countCompletedDeliveries());
        model.addAttribute("lowStockCount", productService.countLowStockProducts());

        // Contextual Lists
        model.addAttribute("lowStockProducts", productService.getLowStockProducts());
        model.addAttribute("recentOrders", orderService.getRecentOrders());
        model.addAttribute("pendingDeliveriesList", deliveryService.getPendingDeliveries());

        return "dashboard";
    }
}
