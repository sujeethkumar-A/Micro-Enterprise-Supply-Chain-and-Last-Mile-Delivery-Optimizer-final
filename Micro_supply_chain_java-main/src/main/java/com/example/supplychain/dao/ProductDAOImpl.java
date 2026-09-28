package com.example.supplychain.dao;

import com.example.supplychain.model.Product;
import com.example.supplychain.repository.ProductRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductDAOImpl implements ProductDAO {

    private final ProductRepository productRepository;

    public ProductDAOImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<Product> findLowStockProducts() {
        return productRepository.findLowStockProducts();
    }

    @Override
    public long countLowStockProducts() {
        return productRepository.countLowStockProducts();
    }

    @Override
    public void updateStock(Long productId, int quantityChange) {
        productRepository.findById(productId).ifPresent(product -> {
            int newQuantity = product.getStockQuantity() + quantityChange;
            if (newQuantity < 0) {
                newQuantity = 0;
            }
            product.setStockQuantity(newQuantity);
            productRepository.save(product);
        });
    }
}
