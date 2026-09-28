package com.example.supplychain.rest;

import com.example.supplychain.dto.BalanceSheetDTO;
import com.example.supplychain.dto.BudgetReportDTO;
import com.example.supplychain.dto.ProfitLossDTO;
import com.example.supplychain.model.ArtisanCluster;
import com.example.supplychain.model.Budget;
import com.example.supplychain.repository.ArtisanClusterRepository;
import com.example.supplychain.repository.BudgetRepository;
import com.example.supplychain.service.ReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportRestController {

    private final ReportService reportService;
    private final ArtisanClusterRepository artisanClusterRepository;
    private final BudgetRepository budgetRepository;

    public ReportRestController(ReportService reportService, ArtisanClusterRepository artisanClusterRepository, BudgetRepository budgetRepository) {
        this.reportService = reportService;
        this.artisanClusterRepository = artisanClusterRepository;
        this.budgetRepository = budgetRepository;
    }

    @GetMapping("/balance-sheet")
    public BalanceSheetDTO getBalanceSheet() {
        double stockValue = 12000.0;
        double cashBalance = 9000.0;
        double pendingPayables = 4500.0;
        return reportService.calculateBalanceSheet(stockValue, cashBalance, pendingPayables);
    }

    @GetMapping("/profit-loss")
    public ProfitLossDTO getProfitLoss() {
        return reportService.calculateProfitLoss(25000.0, 15000.0, 3000.0);
    }

    @GetMapping("/budget")
    public List<BudgetReportDTO> getBudgetReport() {
        List<BudgetReportDTO> reports = new ArrayList<>();
        for (ArtisanCluster cluster : artisanClusterRepository.findAll()) {
            Budget budget = budgetRepository.findByClusterId(cluster.getId()).orElse(null);
            if (budget != null) {
                reports.add(reportService.calculateBudgetReport(cluster.getName(), budget.getPlannedBudget(), budget.getProcurementCost(), budget.getShippingCost()));
            }
        }
        if (reports.isEmpty()) {
            reports.add(reportService.calculateBudgetReport("Southern Craft Cluster", 100000.0, 60000.0, 15000.0));
        }
        return reports;
    }

    @GetMapping("/cluster-profit-loss")
    public List<ProfitLossDTO> getClusterProfitLoss() {
        List<ProfitLossDTO> list = new ArrayList<>();
        list.add(reportService.calculateProfitLoss(25000.0, 15000.0, 3000.0));
        return list;
    }
}
