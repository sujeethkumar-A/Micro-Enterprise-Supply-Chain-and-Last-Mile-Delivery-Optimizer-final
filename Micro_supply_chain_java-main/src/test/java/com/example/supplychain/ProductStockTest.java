package com.example.supplychain;

import com.example.supplychain.model.Product;
import com.example.supplychain.model.Supplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductStockTest {

    @Test
    @DisplayName("Verify Product Creation and Attribute Mapping")
    void testProductCreation() {
        Supplier supplier = new Supplier("Vendor Corp", "Jane Doe", "+91 99999 11111", "jane@vendor.com", "Industrial Zone");
        Product product = new Product("Industrial Packaging Box", "Packaging", 50.0, 100, 20, "Heavy-duty carton", supplier);

        assertNotNull(product);
        assertEquals("Industrial Packaging Box", product.getName());
        assertEquals("Packaging", product.getCategory());
        assertEquals(50.0, product.getPrice());
        assertEquals(100, product.getStockQuantity());
        assertEquals(20, product.getMinimumStock());
        assertEquals(supplier, product.getSupplier());
        assertFalse(product.isLowStock(), "Product with 100 units and min 20 should not be low stock");
    }

    @Test
    @DisplayName("Verify Low Stock Detection: stock <= minimumStock")
    void testLowStockDetection() {
        Product normalStock = new Product("Tape", "Packaging", 20.0, 15, 10, null, null);
        assertFalse(normalStock.isLowStock());

        // Exact match with minimum threshold is low stock
        Product thresholdStock = new Product("Tape", "Packaging", 20.0, 10, 10, null, null);
        assertTrue(thresholdStock.isLowStock(), "Stock equal to minimum threshold should trigger low stock");

        // Below minimum threshold
        Product criticalStock = new Product("Tape", "Packaging", 20.0, 4, 10, null, null);
        assertTrue(criticalStock.isLowStock(), "Stock below minimum threshold must trigger low stock");
    }
}
