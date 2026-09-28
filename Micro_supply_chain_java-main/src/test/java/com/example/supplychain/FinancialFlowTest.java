package com.example.supplychain;

import com.example.supplychain.dto.BalanceSheetDTO;
import com.example.supplychain.dto.BudgetReportDTO;
import com.example.supplychain.dto.ProfitLossDTO;
import com.example.supplychain.service.ReportService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FinancialFlowTest {

    @Test
    void shouldCalculateProfitAndBudgetMetricsFromStoredTransactions() {
        ReportService reportService = new ReportService();

        ProfitLossDTO profitLoss = reportService.calculateProfitLoss(25000.0, 15000.0, 3000.0);
        assertEquals(7000.0, profitLoss.getNetProfit(), 0.01);
        assertEquals(28.0, profitLoss.getProfitMarginPercent(), 0.01);

        BalanceSheetDTO balanceSheet = reportService.calculateBalanceSheet(12000.0, 9000.0, 4500.0);
        assertEquals(12000.0, balanceSheet.getStockValue(), 0.01);
        assertEquals(9000.0, balanceSheet.getCashBalance(), 0.01);
        assertEquals(4500.0, balanceSheet.getPendingPayables(), 0.01);

        BudgetReportDTO budgetReport = reportService.calculateBudgetReport("Southern Craft Cluster", 100000.0, 60000.0, 15000.0);
        assertEquals(75000.0, budgetReport.getActualCost(), 0.01);
        assertEquals(25000.0, budgetReport.getRemainingBudget(), 0.01);
        assertEquals(75.0, budgetReport.getUtilizationPercent(), 0.01);

        List<BudgetReportDTO> reports = List.of(budgetReport);
        assertEquals(1, reports.size());
    }
}
