package com.example.supplychain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "purchase_order")
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "artisan_id", nullable = false)
    private ArtisanContact artisan;

    @Column(nullable = false)
    private LocalDateTime orderDate = LocalDateTime.now();

    @Column(nullable = false)
    private Double totalAmount = 0.0;

    @Column(nullable = false)
    private String status = "CREATED";

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseOrderItem> items = new ArrayList<>();

    public PurchaseOrder() {
    }

    public PurchaseOrder(ArtisanContact artisan, LocalDateTime orderDate, Double totalAmount, String status) {
        this.artisan = artisan;
        this.orderDate = orderDate != null ? orderDate : LocalDateTime.now();
        this.totalAmount = totalAmount != null ? totalAmount : 0.0;
        this.status = status != null ? status : "CREATED";
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public ArtisanContact getArtisan() { return artisan; }
    public void setArtisan(ArtisanContact artisan) { this.artisan = artisan; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public List<PurchaseOrderItem> getItems() { return items; }
    public void setItems(List<PurchaseOrderItem> items) { this.items = items; }
}
