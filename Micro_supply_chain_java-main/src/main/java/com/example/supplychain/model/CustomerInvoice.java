package com.example.supplychain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer_invoice")
public class CustomerInvoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", nullable = false)
    private CustomerOrder order;

    @Column(name = "invoice_date", nullable = false)
    private LocalDateTime invoiceDate = LocalDateTime.now();

    @Column(nullable = false)
    private Double amount;

    @Column(name = "payment_status", nullable = false)
    private String paymentStatus = "UNPAID";

    public CustomerInvoice() {
    }

    public CustomerInvoice(Customer customer, CustomerOrder order, Double amount) {
        this.customer = customer;
        this.order = order;
        this.amount = amount;
        this.invoiceDate = LocalDateTime.now();
        this.paymentStatus = "UNPAID";
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    public CustomerOrder getOrder() { return order; }
    public void setOrder(CustomerOrder order) { this.order = order; }
    public LocalDateTime getInvoiceDate() { return invoiceDate; }
    public void setInvoiceDate(LocalDateTime invoiceDate) { this.invoiceDate = invoiceDate; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
}
