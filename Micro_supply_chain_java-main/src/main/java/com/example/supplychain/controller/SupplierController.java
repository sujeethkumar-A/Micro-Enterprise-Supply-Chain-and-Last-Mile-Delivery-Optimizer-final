package com.example.supplychain.controller;

import com.example.supplychain.model.Supplier;
import com.example.supplychain.service.SupplierService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping
    public String listSuppliers(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size,
                                @RequestParam(required = false) String search,
                                Model model) {
        Page<Supplier> supplierPage = supplierService.getSuppliersPaged(page, size, search);
        model.addAttribute("supplierPage", supplierPage);
        model.addAttribute("suppliers", supplierPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", supplierPage.getTotalPages());
        model.addAttribute("totalItems", supplierPage.getTotalElements());
        model.addAttribute("search", search);
        return "suppliers";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("supplier", new Supplier());
        model.addAttribute("pageTitle", "Add New Supplier");
        return "supplier-form";
    }

    @PostMapping
    public String saveSupplier(@Valid @ModelAttribute("supplier") Supplier supplier,
                               BindingResult result,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", supplier.getId() == null ? "Add New Supplier" : "Edit Supplier");
            return "supplier-form";
        }
        supplierService.createSupplier(supplier);
        redirectAttributes.addFlashAttribute("successMessage", "Supplier '" + supplier.getName() + "' saved successfully!");
        return "redirect:/suppliers";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Supplier supplier = supplierService.getSupplierById(id);
        model.addAttribute("supplier", supplier);
        model.addAttribute("pageTitle", "Edit Supplier: " + supplier.getName());
        return "supplier-form";
    }

    @PostMapping("/update/{id}")
    public String updateSupplier(@PathVariable Long id,
                                 @Valid @ModelAttribute("supplier") Supplier supplier,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Edit Supplier");
            return "supplier-form";
        }
        supplierService.updateSupplier(id, supplier);
        redirectAttributes.addFlashAttribute("successMessage", "Supplier updated successfully!");
        return "redirect:/suppliers";
    }

    @GetMapping("/delete/{id}")
    public String deleteSupplier(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            supplierService.deleteSupplier(id);
            redirectAttributes.addFlashAttribute("successMessage", "Supplier deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cannot delete supplier: It may be associated with existing products.");
        }
        return "redirect:/suppliers";
    }
}
