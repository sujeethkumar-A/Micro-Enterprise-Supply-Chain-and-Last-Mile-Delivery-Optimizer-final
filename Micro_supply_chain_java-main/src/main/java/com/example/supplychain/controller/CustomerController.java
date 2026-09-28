package com.example.supplychain.controller;

import com.example.supplychain.model.Customer;
import com.example.supplychain.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public String listCustomers(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size,
                                @RequestParam(required = false) String search,
                                Model model) {
        Page<Customer> customerPage = customerService.getCustomersPaged(page, size, search);
        model.addAttribute("customerPage", customerPage);
        model.addAttribute("customers", customerPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", customerPage.getTotalPages());
        model.addAttribute("totalItems", customerPage.getTotalElements());
        model.addAttribute("search", search);
        return "customers";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("customer", new Customer());
        model.addAttribute("pageTitle", "Add New Customer");
        return "customer-form";
    }

    @PostMapping
    public String saveCustomer(@Valid @ModelAttribute("customer") Customer customer,
                               BindingResult result,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", customer.getId() == null ? "Add New Customer" : "Edit Customer");
            return "customer-form";
        }
        customerService.createCustomer(customer);
        redirectAttributes.addFlashAttribute("successMessage", "Customer '" + customer.getName() + "' saved successfully!");
        return "redirect:/customers";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Customer customer = customerService.getCustomerById(id);
        model.addAttribute("customer", customer);
        model.addAttribute("pageTitle", "Edit Customer: " + customer.getName());
        return "customer-form";
    }

    @PostMapping("/update/{id}")
    public String updateCustomer(@PathVariable Long id,
                                 @Valid @ModelAttribute("customer") Customer customer,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Edit Customer");
            return "customer-form";
        }
        customerService.updateCustomer(id, customer);
        redirectAttributes.addFlashAttribute("successMessage", "Customer updated successfully!");
        return "redirect:/customers";
    }

    @GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            customerService.deleteCustomer(id);
            redirectAttributes.addFlashAttribute("successMessage", "Customer deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cannot delete customer: It has associated orders or deliveries.");
        }
        return "redirect:/customers";
    }
}
