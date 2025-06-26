package com.tracker.finance.modules.recurringexpense.service.impl;

import com.tracker.finance.core.exception.specific_exceptions.ResourceNotFoundException;
import com.tracker.finance.core.security.exception.AccessDeniedException;
import com.tracker.finance.modules.recurringexpense.RecurringExpense;
import com.tracker.finance.modules.recurringexpense.RecurringExpense.Frequency;
import com.tracker.finance.modules.recurringexpense.dto.CreateRecurringExpenseRequest;
import com.tracker.finance.modules.recurringexpense.dto.RecurringExpenseDto;
import com.tracker.finance.modules.recurringexpense.mapper.RecurringExpenseMapper;
import com.tracker.finance.modules.recurringexpense.RecurringExpenseRepository;
import com.tracker.finance.modules.recurringexpense.service.RecurringExpenseService;
import com.tracker.finance.modules.user.User;
import com.tracker.finance.modules.user.UserRepository;
import com.tracker.finance.core.exception.specific_exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecurringExpenseServiceImpl implements RecurringExpenseService {

    private final RecurringExpenseRepository recurringExpenseRepository;
    private final UserRepository userRepository;
    private final RecurringExpenseMapper recurringExpenseMapper;

    @Override
    @Transactional
    public RecurringExpenseDto addRecurringExpense(CreateRecurringExpenseRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        validateRecurringExpenseRequest(request);

        RecurringExpense recurringExpense = recurringExpenseMapper.fromRequest(request);
        recurringExpense.setUser(user);

        RecurringExpense savedExpense = recurringExpenseRepository.save(recurringExpense);
        return recurringExpenseMapper.toDto(savedExpense);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecurringExpenseDto> findRecurringExpensesByUser(String username) {
        return recurringExpenseRepository.findByUser_Username(username).stream()
                .map(recurringExpenseMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteRecurringExpense(UUID id, String username) {
        RecurringExpense expense = recurringExpenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recurring expense not found with id: " + id));

        if (!expense.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("User is not authorized to delete this resource.");
        }

        recurringExpenseRepository.delete(expense);
    }

    private void validateRecurringExpenseRequest(CreateRecurringExpenseRequest request) {
        int day = request.getDayOfPeriod();
        if (request.getFrequency() == Frequency.WEEKLY && (day < 1 || day > 7)) {
            throw new BadRequestException(
                    "For weekly frequency, day of period must be between 1 (Monday) and 7 (Sunday).");
        }
        if (request.getFrequency() == Frequency.MONTHLY && (day < 1 || day > 31)) {
            throw new BadRequestException("For monthly frequency, day of period must be between 1 and 31.");
        }
    }
}