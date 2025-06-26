package com.tracker.finance.modules.transaction.service.impl;

import com.tracker.finance.core.exception.specific_exceptions.ResourceNotFoundException;
import com.tracker.finance.core.security.exception.AccessDeniedException;
import com.tracker.finance.modules.transaction.Transaction;
import com.tracker.finance.modules.transaction.dto.CreateTransactionRequest;
import com.tracker.finance.modules.transaction.dto.TransactionDto;
import com.tracker.finance.modules.transaction.mapper.TransactionMapper;
import com.tracker.finance.modules.transaction.TransactionRepository;
import com.tracker.finance.modules.transaction.service.TransactionService;
import com.tracker.finance.modules.user.User;
import com.tracker.finance.modules.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final TransactionMapper transactionMapper;

    @Override
    public List<TransactionDto> findAllTransactionsByUser(String username) {
        return transactionRepository.findByUser_UsernameOrderByDateDesc(username).stream()
                .map(transactionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TransactionDto findTransactionById(UUID id, String username) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        // Authorization check
        if (!transaction.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("Unauthorized");
        }
        return transactionMapper.toDto(transaction);
    }

    @Override
    public TransactionDto addTransaction(CreateTransactionRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Transaction transaction = transactionMapper.fromRequest(request);
        transaction.setUser(user);
        return transactionMapper.toDto(transactionRepository.save(transaction));
    }

    @Override
    public TransactionDto updateTransaction(UUID id, CreateTransactionRequest request, String username) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        if (!transaction.getUser().getUsername().equals(username))
            throw new AccessDeniedException("Unauthorized");

        transactionMapper.updateFromRequest(transaction, request);
        return transactionMapper.toDto(transactionRepository.save(transaction));
    }

    @Override
    public void deleteTransaction(UUID id, String username) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
        if (!transaction.getUser().getUsername().equals(username))
            throw new AccessDeniedException("Unauthorized");
        transactionRepository.delete(transaction);
    }
}
