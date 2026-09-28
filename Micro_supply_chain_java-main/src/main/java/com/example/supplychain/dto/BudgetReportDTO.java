package com.example.supplychain.dto;

public class BudgetReportDTO {
    private String clusterName;
    private Double plannedBudget;
    private Double procurementCost;
    private Double shippingCost;
    private Double actualCost;
    private Double remainingBudget;
    private Double utilizationPercent;

    public BudgetReportDTO() {
    }

    public BudgetReportDTO(String clusterName, Double plannedBudget, Double procurementCost, Double shippingCost) {
        this.clusterName = clusterName;
        this.plannedBudget = plannedBudget;
        this.procurementCost = procurementCost;
        this.shippingCost = shippingCost;
        this.actualCost = procurementCost + shippingCost;
        this.remainingBudget = plannedBudget - this.actualCost;
        this.utilizationPercent = plannedBudget > 0 ? (this.actualCost / plannedBudget) * 100 : 0.0;
    }

    public String getClusterName() { return clusterName; }
    public void setClusterName(String clusterName) { this.clusterName = clusterName; }
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
    public Double getUtilizationPercent() { return utilizationPercent; }
    public void setUtilizationPercent(Double utilizationPercent) { this.utilizationPercent = utilizationPercent; }
}
