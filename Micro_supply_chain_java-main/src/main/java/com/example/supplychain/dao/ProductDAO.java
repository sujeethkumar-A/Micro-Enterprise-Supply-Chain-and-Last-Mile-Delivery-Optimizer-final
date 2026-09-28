package com.example.supplychain.dao;

import com.example.supplychain.model.Product;
import java.util.List;
import java.util.Optional;

public interface ProductDAO {
    Product save(Product product);
    Optional<Product> findById(Long id);
    List<Product> findAll();
    void deleteById(Long id);
    List<Product> findLowStockProducts();
    long countLowStockProducts();
    void updateStock(Long productId, int quantityChange);
}
