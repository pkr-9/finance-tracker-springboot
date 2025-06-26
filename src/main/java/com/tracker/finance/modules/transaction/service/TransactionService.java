package com.tracker.finance.modules.transaction.service;

import com.tracker.finance.modules.transaction.dto.CreateTransactionRequest;
import com.tracker.finance.modules.transaction.dto.TransactionDto;

import java.util.List;
import java.util.UUID;

public interface TransactionService {
    List<TransactionDto> findAllTransactionsByUser(String username);

    TransactionDto findTransactionById(UUID transactionId, String username);

    TransactionDto addTransaction(CreateTransactionRequest request, String username);

    TransactionDto updateTransaction(UUID transactionId, CreateTransactionRequest request, String username);

    void deleteTransaction(UUID transactionId, String username);
}
