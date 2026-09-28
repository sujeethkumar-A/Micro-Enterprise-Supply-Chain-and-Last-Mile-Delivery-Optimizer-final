package com.example.supplychain.controller;

import com.example.supplychain.dto.OrderCreateRequest;
import com.example.supplychain.dto.OrderItemRequest;
import com.example.supplychain.enums.OrderStatus;
import com.example.supplychain.exception.InsufficientStockException;
import com.example.supplychain.model.CustomerOrder;
import com.example.supplychain.service.CustomerService;
import com.example.supplychain.service.OrderService;
import com.example.supplychain.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final CustomerService customerService;
    private final ProductService productService;

    public OrderController(OrderService orderService,
                           CustomerService customerService,
                           ProductService productService) {
        this.orderService = orderService;
        this.customerService = customerService;
        this.productService = productService;
    }

    @GetMapping
    public String listOrders(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size,
                             @RequestParam(required = false) OrderStatus status,
                             Model model) {
        Page<CustomerOrder> orderPage = orderService.getOrdersPaged(page, size, status);
        model.addAttribute("orderPage", orderPage);
        model.addAttribute("orders", orderPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", orderPage.getTotalPages());
        model.addAttribute("totalItems", orderPage.getTotalElements());
        model.addAttribute("selectedStatus", status);
        model.addAttribute("statuses", OrderStatus.values());
        return "orders";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        OrderCreateRequest orderRequest = new OrderCreateRequest();
        // default 1 item row
        List<OrderItemRequest> initialItems = new ArrayList<>();
        initialItems.add(new OrderItemRequest());
        orderRequest.setItems(initialItems);

        model.addAttribute("orderRequest", orderRequest);
        model.addAttribute("customers", customerService.getAllCustomers());
        model.addAttribute("products", productService.getAllProducts());
        return "order-form";
    }

    @PostMapping
    public String createOrder(@ModelAttribute("orderRequest") OrderCreateRequest orderRequest,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        try {
            CustomerOrder createdOrder = orderService.createOrder(orderRequest);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Order #" + createdOrder.getId() + " placed successfully! Total: ₹" + createdOrder.getTotalAmount());
            return "redirect:/orders/" + createdOrder.getId();
        } catch (InsufficientStockException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("orderRequest", orderRequest);
            model.addAttribute("customers", customerService.getAllCustomers());
            model.addAttribute("products", productService.getAllProducts());
            return "order-form";
        } catch (Exception ex) {
            model.addAttribute("errorMessage", "Failed to place order: " + ex.getMessage());
            model.addAttribute("orderRequest", orderRequest);
            model.addAttribute("customers", customerService.getAllCustomers());
            model.addAttribute("products", productService.getAllProducts());
            return "order-form";
        }
    }

    @GetMapping("/{id}")
    public String viewOrderDetails(@PathVariable Long id, Model model) {
        CustomerOrder order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        model.addAttribute("statuses", OrderStatus.values());
        return "order-details";
    }

    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam OrderStatus status,
                               RedirectAttributes redirectAttributes) {
        orderService.updateOrderStatus(id, status);
        redirectAttributes.addFlashAttribute("successMessage", "Order #" + id + " status updated to " + status);
        return "redirect:/orders/" + id;
    }
}
