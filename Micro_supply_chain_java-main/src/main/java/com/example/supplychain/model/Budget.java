package com.example.supplychain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "budget")
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cluster_id", nullable = false)
    private ArtisanCluster cluster;

    @Column(name = "planned_budget", nullable = false)
    private Double plannedBudget;

    @Column(name = "procurement_cost", nullable = false)
    private Double procurementCost = 0.0;

    @Column(name = "shipping_cost", nullable = false)
    private Double shippingCost = 0.0;

    @Column(name = "actual_cost", nullable = false)
    private Double actualCost = 0.0;

    @Column(name = "remaining_budget", nullable = false)
    private Double remainingBudget;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Budget() {
    }

    public Budget(ArtisanCluster cluster, Double plannedBudget, Double procurementCost, Double shippingCost) {
        this.cluster = cluster;
        this.plannedBudget = plannedBudget;
        this.procurementCost = procurementCost != null ? procurementCost : 0.0;
        this.shippingCost = shippingCost != null ? shippingCost : 0.0;
        this.actualCost = this.procurementCost + this.shippingCost;
        this.remainingBudget = plannedBudget - this.actualCost;
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public ArtisanCluster getCluster() { return cluster; }
    public void setCluster(ArtisanCluster cluster) { this.cluster = cluster; }
    public Double getPlannedBudget() { return plannedBudget; }
    public void setPlannedBudget(Double plannedBudget) { this.plannedBudget = plannedBudget; }
    public Double getProcurementCost() { return procurementCost; }
    public void setProcurementCost(Double procurementCost) { this.procurementCost = procurementCost; }
    public Double getShippingCost() { return shippingCost; }
    public void setShippingCost(Double shippingCost) { this.shippingCost = shippingCost; }
    public Double getActualCost() { return actualCost; }
    public void setActualCost(Double actualCost) { this.actualCost = actualCost; }
    public Double getRemainingBudget() { return remainingBudget; }
    public void setRemainingBudget(Double remainingBudget) { this.remainingBudget = remainingBudget; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
