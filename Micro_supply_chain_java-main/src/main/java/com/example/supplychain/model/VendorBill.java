package com.example.supplychain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "vendor_bill")
public class VendorBill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "purchase_order_id", nullable = false)
    private PurchaseOrder purchaseOrder;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "artisan_id", nullable = false)
    private ArtisanContact artisan;

    @Column(nullable = false)
    private Double amount;

    @Column(name = "bill_date", nullable = false)
    private LocalDateTime billDate = LocalDateTime.now();

    @Column(name = "payment_status", nullable = false)
    private String paymentStatus = "UNPAID";

    public VendorBill() {
    }

    public VendorBill(PurchaseOrder purchaseOrder, ArtisanContact artisan, Double amount) {
        this.purchaseOrder = purchaseOrder;
        this.artisan = artisan;
        this.amount = amount;
        this.billDate = LocalDateTime.now();
        this.paymentStatus = "UNPAID";
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public PurchaseOrder getPurchaseOrder() { return purchaseOrder; }
    public void setPurchaseOrder(PurchaseOrder purchaseOrder) { this.purchaseOrder = purchaseOrder; }
    public ArtisanContact getArtisan() { return artisan; }
    public void setArtisan(ArtisanContact artisan) { this.artisan = artisan; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public LocalDateTime getBillDate() { return billDate; }
    public void setBillDate(LocalDateTime billDate) { this.billDate = billDate; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
}
