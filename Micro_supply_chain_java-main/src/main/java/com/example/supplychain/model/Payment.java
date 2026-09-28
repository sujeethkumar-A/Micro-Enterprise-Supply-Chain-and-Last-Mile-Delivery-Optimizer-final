package com.example.supplychain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "invoice_id")
    private CustomerInvoice invoice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(nullable = false)
    private Double amount;

    @Column(name = "payment_method", nullable = false)
    private String paymentMethod = "BANK_TRANSFER";

    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate = LocalDateTime.now();

    @Column(name = "reference_number")
    private String referenceNumber;

    @Column(nullable = false)
    private String status = "PAID";

    public Payment() {
    }

    public Payment(CustomerInvoice invoice, Customer customer, Double amount, String paymentMethod, String referenceNumber, String status) {
        this.invoice = invoice;
        this.customer = customer;
        this.amount = amount;
        this.paymentMethod = paymentMethod != null ? paymentMethod : "BANK_TRANSFER";
        this.referenceNumber = referenceNumber;
        this.paymentDate = LocalDateTime.now();
        this.status = status != null ? status : "PAID";
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public CustomerInvoice getInvoice() { return invoice; }
    public void setInvoice(CustomerInvoice invoice) { this.invoice = invoice; }
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }
    public String getReferenceNumber() { return referenceNumber; }
    public void setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
