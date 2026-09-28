package com.example.supplychain.dto;

public class ProfitLossDTO {
    private Double revenue;
    private Double artisanCost;
    private Double freightCost;
    private Double netProfit;
    private Double profitMarginPercent;

    public ProfitLossDTO() {
    }

    public ProfitLossDTO(Double revenue, Double artisanCost, Double freightCost, Double netProfit, Double profitMarginPercent) {
        this.revenue = revenue;
        this.artisanCost = artisanCost;
        this.freightCost = freightCost;
        this.netProfit = netProfit;
        this.profitMarginPercent = profitMarginPercent;
    }

    public Double getRevenue() { return revenue; }
    public void setRevenue(Double revenue) { this.revenue = revenue; }
    public Double getArtisanCost() { return artisanCost; }
    public void setArtisanCost(Double artisanCost) { this.artisanCost = artisanCost; }
    public Double getFreightCost() { return freightCost; }
    public void setFreightCost(Double freightCost) { this.freightCost = freightCost; }
    public Double getNetProfit() { return netProfit; }
    public void setNetProfit(Double netProfit) { this.netProfit = netProfit; }
    public Double getProfitMarginPercent() { return profitMarginPercent; }
    public void setProfitMarginPercent(Double profitMarginPercent) { this.profitMarginPercent = profitMarginPercent; }
}
