package com.tracker.finance.modules.transaction.dto;

import com.tracker.finance.modules.transaction.Transaction;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class TransactionDto {
    private UUID id;
    private BigDecimal amount;
    private Transaction.TransactionType type;
    private String category;
    private LocalDate date;
    private String notes;
}