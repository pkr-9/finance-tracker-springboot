package com.tracker.finance.modules.budget.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class BudgetDto {
    private UUID id;
    private String category;
    private BigDecimal limitAmount;
    private LocalDate month;
}