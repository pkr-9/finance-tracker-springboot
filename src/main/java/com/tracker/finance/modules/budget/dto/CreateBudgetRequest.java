package com.tracker.finance.modules.budget.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.YearMonth;

@Data
public class CreateBudgetRequest {

    @NotEmpty
    private String category;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal limitAmount;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM")
    private YearMonth month;
}