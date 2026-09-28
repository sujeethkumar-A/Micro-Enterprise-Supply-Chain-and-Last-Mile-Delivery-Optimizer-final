package com.example.supplychain.service;

import com.example.supplychain.dto.BalanceSheetDTO;
import com.example.supplychain.dto.BudgetReportDTO;
import com.example.supplychain.dto.ProfitLossDTO;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

    public ProfitLossDTO calculateProfitLoss(Double revenue, Double artisanCost, Double freightCost) {
        double netProfit = revenue - artisanCost - freightCost;
        double margin = revenue > 0 ? (netProfit / revenue) * 100 : 0.0;
        return new ProfitLossDTO(revenue, artisanCost, freightCost, netProfit, margin);
    }

    public BalanceSheetDTO calculateBalanceSheet(Double stockValue, Double cashBalance, Double pendingPayables) {
        return new BalanceSheetDTO(stockValue, cashBalance, pendingPayables);
    }

    public BudgetReportDTO calculateBudgetReport(String clusterName, Double plannedBudget, Double procurementCost, Double shippingCost) {
        return new BudgetReportDTO(clusterName, plannedBudget, procurementCost, shippingCost);
    }
}
