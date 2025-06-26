package com.tracker.finance.modules.recurringexpense.dto;

import com.tracker.finance.modules.recurringexpense.RecurringExpense;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateRecurringExpenseRequest {
    @NotEmpty
    private String title;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal amount;

    @NotNull
    private RecurringExpense.Frequency frequency;

    @NotNull
    @Min(value = 1, message = "Day must be at least 1")
    @Max(value = 31, message = "Day must be at most 31")
    private Integer dayOfPeriod;

    @NotEmpty
    private String category;
}