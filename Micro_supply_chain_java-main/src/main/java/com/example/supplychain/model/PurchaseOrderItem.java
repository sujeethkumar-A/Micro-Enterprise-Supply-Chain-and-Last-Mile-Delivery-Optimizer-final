package com.example.supplychain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "purchase_order_item")
public class PurchaseOrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_id", nullable = false)
    private PurchaseOrder purchaseOrder;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "unit_cost", nullable = false)
    private Double unitCost;

    @Column(nullable = false)
    private Double totalAmount;

    public PurchaseOrderItem() {
    }

    public PurchaseOrderItem(PurchaseOrder purchaseOrder, Product product, Integer quantity, Double unitCost) {
        this.purchaseOrder = purchaseOrder;
        this.product = product;
        this.quantity = quantity;
        this.unitCost = unitCost;
        this.totalAmount = quantity * unitCost;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public PurchaseOrder getPurchaseOrder() { return purchaseOrder; }
    public void setPurchaseOrder(PurchaseOrder purchaseOrder) { this.purchaseOrder = purchaseOrder; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Double getUnitCost() { return unitCost; }
    public void setUnitCost(Double unitCost) { this.unitCost = unitCost; }
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
}
