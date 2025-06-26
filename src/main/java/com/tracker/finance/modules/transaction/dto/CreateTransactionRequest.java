package com.tracker.finance.modules.transaction.dto;

import com.tracker.finance.modules.transaction.Transaction;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CreateTransactionRequest {
    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal amount;

    @NotNull
    private Transaction.TransactionType type;

    @NotEmpty
    private String category;

    @NotNull
    private LocalDate date;

    private String notes;
}