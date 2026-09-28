package com.example.supplychain.dto;

public class BalanceSheetDTO {
    private Double stockValue;
    private Double cashBalance;
    private Double pendingPayables;
    private Double totalAssets;
    private Double totalLiabilities;

    public BalanceSheetDTO() {
    }

    public BalanceSheetDTO(Double stockValue, Double cashBalance, Double pendingPayables) {
        this.stockValue = stockValue;
        this.cashBalance = cashBalance;
        this.pendingPayables = pendingPayables;
        this.totalAssets = stockValue + cashBalance;
        this.totalLiabilities = pendingPayables;
    }

    public Double getStockValue() { return stockValue; }
    public void setStockValue(Double stockValue) { this.stockValue = stockValue; }
    public Double getCashBalance() { return cashBalance; }
    public void setCashBalance(Double cashBalance) { this.cashBalance = cashBalance; }
    public Double getPendingPayables() { return pendingPayables; }
    public void setPendingPayables(Double pendingPayables) { this.pendingPayables = pendingPayables; }
    public Double getTotalAssets() { return totalAssets; }
    public void setTotalAssets(Double totalAssets) { this.totalAssets = totalAssets; }
    public Double getTotalLiabilities() { return totalLiabilities; }
    public void setTotalLiabilities(Double totalLiabilities) { this.totalLiabilities = totalLiabilities; }
}
