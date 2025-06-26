package com.tracker.finance.modules.transaction.mapper;

import com.tracker.finance.modules.transaction.Transaction;
import com.tracker.finance.modules.transaction.dto.CreateTransactionRequest;
import com.tracker.finance.modules.transaction.dto.TransactionDto;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionDto toDto(Transaction t) {
        return TransactionDto.builder()
                .id(t.getId())
                .amount(t.getAmount())
                .type(t.getType())
                .category(t.getCategory())
                .date(t.getDate())
                .notes(t.getNotes())
                .build();
    }

    public Transaction fromRequest(CreateTransactionRequest r) {
        Transaction t = new Transaction();
        t.setAmount(r.getAmount());
        t.setCategory(r.getCategory());
        t.setDate(r.getDate());
        t.setNotes(r.getNotes());
        t.setType(r.getType());
        return t;
    }

    public void updateFromRequest(Transaction t, CreateTransactionRequest r) {
        t.setAmount(r.getAmount());
        t.setCategory(r.getCategory());
        t.setDate(r.getDate());
        t.setNotes(r.getNotes());
        t.setType(r.getType());
    }
}
