package com.example.supplychain.service;

import com.example.supplychain.dao.ProductDAO;
import com.example.supplychain.dto.ProductDTO;
import com.example.supplychain.exception.ResourceNotFoundException;
import com.example.supplychain.model.Product;
import com.example.supplychain.model.Supplier;
import com.example.supplychain.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductDAO productDAO;
    private final SupplierService supplierService;

    public ProductService(ProductRepository productRepository, ProductDAO productDAO, SupplierService supplierService) {
        this.productRepository = productRepository;
        this.productDAO = productDAO;
        this.supplierService = supplierService;
    }

    public Product createProduct(Product product, Long supplierId) {
        Supplier supplier = supplierService.getSupplierById(supplierId);
        product.setSupplier(supplier);
        return productDAO.save(product);
    }

    public Product updateProduct(Long id, Product updatedProduct, Long supplierId) {
        Product existing = getProductById(id);
        existing.setName(updatedProduct.getName());
        existing.setCategory(updatedProduct.getCategory());
        existing.setPrice(updatedProduct.getPrice());
        existing.setStockQuantity(updatedProduct.getStockQuantity());
        existing.setMinimumStock(updatedProduct.getMinimumStock());
        existing.setDescription(updatedProduct.getDescription());

        if (supplierId != null) {
            Supplier supplier = supplierService.getSupplierById(supplierId);
            existing.setSupplier(supplier);
        }

        return productDAO.save(existing);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with ID: " + id);
        }
        productDAO.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Product getProductById(Long id) {
        return productDAO.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productDAO.findAll();
    }

    @Transactional(readOnly = true)
    public Page<Product> getProductsPaged(int page, int size, String search, String category) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "name"));
        if (search != null && !search.trim().isEmpty()) {
            return productRepository.findByNameContainingIgnoreCase(search.trim(), pageable);
        }
        if (category != null && !category.trim().isEmpty()) {
            return productRepository.findByCategoryIgnoreCase(category.trim(), pageable);
        }
        return productRepository.findAll(pageable);
    }

    public void updateStock(Long id, int quantityChange) {
        Product product = getProductById(id);
        int newQuantity = product.getStockQuantity() + quantityChange;
        if (newQuantity < 0) {
            newQuantity = 0;
        }
        product.setStockQuantity(newQuantity);
        productDAO.save(product);
    }

    @Transactional(readOnly = true)
    public List<Product> getLowStockProducts() {
        return productDAO.findLowStockProducts();
    }

    @Transactional(readOnly = true)
    public long countLowStockProducts() {
        return productDAO.countLowStockProducts();
    }

    @Transactional(readOnly = true)
    public long countProducts() {
        return productRepository.count();
    }

    @Transactional(readOnly = true)
    public List<String> getAllCategories() {
        return productRepository.findDistinctCategories();
    }

    public ProductDTO toDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setCategory(product.getCategory());
        dto.setPrice(product.getPrice());
        dto.setStockQuantity(product.getStockQuantity());
        dto.setMinimumStock(product.getMinimumStock());
        dto.setDescription(product.getDescription());
        if (product.getSupplier() != null) {
            dto.setSupplierId(product.getSupplier().getId());
            dto.setSupplierName(product.getSupplier().getName());
        }
        dto.setLowStock(product.isLowStock());
        return dto;
    }
}
