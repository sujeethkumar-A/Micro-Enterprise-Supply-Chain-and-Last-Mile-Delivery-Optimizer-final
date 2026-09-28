package com.example.supplychain.controller;

import com.example.supplychain.model.Product;
import com.example.supplychain.model.Supplier;
import com.example.supplychain.service.ProductService;
import com.example.supplychain.service.SupplierService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final SupplierService supplierService;

    public ProductController(ProductService productService, SupplierService supplierService) {
        this.productService = productService;
        this.supplierService = supplierService;
    }

    @GetMapping
    public String listProducts(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(required = false) String search,
                               @RequestParam(required = false) String category,
                               @RequestParam(required = false, defaultValue = "false") boolean lowStockOnly,
                               Model model) {
        if (lowStockOnly) {
            List<Product> lowStockList = productService.getLowStockProducts();
            model.addAttribute("products", lowStockList);
            model.addAttribute("lowStockOnly", true);
            model.addAttribute("totalItems", lowStockList.size());
            model.addAttribute("totalPages", 1);
            model.addAttribute("currentPage", 0);
        } else {
            Page<Product> productPage = productService.getProductsPaged(page, size, search, category);
            model.addAttribute("productPage", productPage);
            model.addAttribute("products", productPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", productPage.getTotalPages());
            model.addAttribute("totalItems", productPage.getTotalElements());
            model.addAttribute("lowStockOnly", false);
        }

        model.addAttribute("search", search);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("categories", productService.getAllCategories());
        return "products";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("suppliers", supplierService.getAllSuppliers());
        model.addAttribute("pageTitle", "Add New Product");
        return "product-form";
    }

    @PostMapping
    public String saveProduct(@Valid @ModelAttribute("product") Product product,
                              BindingResult result,
                              @RequestParam(required = false) Long supplierId,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        if (supplierId == null) {
            result.rejectValue("supplier", "error.product", "Supplier selection is required");
        }
        if (result.hasErrors()) {
            model.addAttribute("suppliers", supplierService.getAllSuppliers());
            model.addAttribute("pageTitle", "Add New Product");
            return "product-form";
        }
        productService.createProduct(product, supplierId);
        redirectAttributes.addFlashAttribute("successMessage", "Product '" + product.getName() + "' saved successfully!");
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("suppliers", supplierService.getAllSuppliers());
        model.addAttribute("pageTitle", "Edit Product: " + product.getName());
        return "product-form";
    }

    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id,
                                @Valid @ModelAttribute("product") Product product,
                                BindingResult result,
                                @RequestParam(required = false) Long supplierId,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        if (supplierId == null) {
            result.rejectValue("supplier", "error.product", "Supplier selection is required");
        }
        if (result.hasErrors()) {
            model.addAttribute("suppliers", supplierService.getAllSuppliers());
            model.addAttribute("pageTitle", "Edit Product");
            return "product-form";
        }
        productService.updateProduct(id, product, supplierId);
        redirectAttributes.addFlashAttribute("successMessage", "Product updated successfully!");
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("successMessage", "Product deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cannot delete product: It is associated with orders or deliveries.");
        }
        return "redirect:/products";
    }

    @PostMapping("/{id}/stock")
    public String adjustStock(@PathVariable Long id,
                              @RequestParam int change,
                              RedirectAttributes redirectAttributes) {
        productService.updateStock(id, change);
        redirectAttributes.addFlashAttribute("successMessage", "Stock updated successfully!");
        return "redirect:/products";
    }
}
