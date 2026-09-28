package com.example.supplychain.rest;

import com.example.supplychain.dto.ProductDTO;
import com.example.supplychain.model.Product;
import com.example.supplychain.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    private final ProductService productService;

    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts().stream()
                .map(productService::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ProductDTO getProductById(@PathVariable Long id) {
        return productService.toDTO(productService.getProductById(id));
    }

    @GetMapping("/low-stock")
    public List<ProductDTO> getLowStockProducts() {
        return productService.getLowStockProducts().stream()
                .map(productService::toDTO)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setCategory(productDTO.getCategory());
        product.setPrice(productDTO.getPrice());
        product.setStockQuantity(productDTO.getStockQuantity());
        product.setMinimumStock(productDTO.getMinimumStock());
        product.setDescription(productDTO.getDescription());

        Product saved = productService.createProduct(product, productDTO.getSupplierId());
        return new ResponseEntity<>(productService.toDTO(saved), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setCategory(productDTO.getCategory());
        product.setPrice(productDTO.getPrice());
        product.setStockQuantity(productDTO.getStockQuantity());
        product.setMinimumStock(productDTO.getMinimumStock());
        product.setDescription(productDTO.getDescription());

        Product updated = productService.updateProduct(id, product, productDTO.getSupplierId());
        return ResponseEntity.ok(productService.toDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
