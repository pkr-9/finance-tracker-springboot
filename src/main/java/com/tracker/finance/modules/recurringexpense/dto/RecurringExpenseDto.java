package com.tracker.finance.modules.recurringexpense.dto;

import com.tracker.finance.modules.recurringexpense.RecurringExpense;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class RecurringExpenseDto {
    private UUID id;
    private String title;
    private BigDecimal amount;
    private RecurringExpense.Frequency frequency;
    private int dayOfPeriod;
    private String category;
}