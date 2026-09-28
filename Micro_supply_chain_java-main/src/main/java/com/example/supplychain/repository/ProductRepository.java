package com.example.supplychain.repository;

import com.example.supplychain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
    Page<Product> findByCategoryIgnoreCase(String category, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.stockQuantity <= p.minimumStock")
    List<Product> findLowStockProducts();

    @Query("SELECT COUNT(p) FROM Product p WHERE p.stockQuantity <= p.minimumStock")
    long countLowStockProducts();

    @Query("SELECT DISTINCT p.category FROM Product p ORDER BY p.category ASC")
    List<String> findDistinctCategories();
}
