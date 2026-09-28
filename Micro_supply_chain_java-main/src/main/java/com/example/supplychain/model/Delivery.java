package com.example.supplychain.model;

import com.example.supplychain.enums.DeliveryStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "delivery")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", nullable = false)
    private CustomerOrder order;

    @Column(name = "delivery_date")
    private LocalDateTime deliveryDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus status;

    @Column(nullable = false)
    private Double distance; // in km

    @Column(name = "estimated_cost", nullable = false)
    private Double estimatedCost; // in ₹

    @Column(name = "estimated_time", nullable = false)
    private Double estimatedTime; // in minutes

    public Delivery() {
        this.status = DeliveryStatus.PENDING;
        this.distance = 0.0;
        this.estimatedCost = 0.0;
        this.estimatedTime = 0.0;
    }

    public Delivery(CustomerOrder order, LocalDateTime deliveryDate, DeliveryStatus status, Double distance, Double estimatedCost, Double estimatedTime) {
        this.order = order;
        this.deliveryDate = deliveryDate;
        this.status = status != null ? status : DeliveryStatus.PENDING;
        this.distance = distance != null ? distance : 0.0;
        this.estimatedCost = estimatedCost != null ? estimatedCost : 0.0;
        this.estimatedTime = estimatedTime != null ? estimatedTime : 0.0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CustomerOrder getOrder() {
        return order;
    }

    public void setOrder(CustomerOrder order) {
        this.order = order;
    }

    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(Double estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

    public Double getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(Double estimatedTime) {
        this.estimatedTime = estimatedTime;
    }
}
