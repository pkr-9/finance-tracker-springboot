package com.tracker.finance.modules.analytics.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
public class MonthlySummaryDto {
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal savings;
    private Map<String, BigDecimal> expensesByCategory;
    private Map<String, BudgetStatus> budgetStatus;

    @Data
    @Builder
    public static class BudgetStatus {
        private BigDecimal spent;
        private BigDecimal budgetLimit;
        private BigDecimal remaining;
    }
}