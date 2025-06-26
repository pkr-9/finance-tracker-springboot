package com.tracker.finance.modules.recurringexpense.service;

import com.tracker.finance.modules.recurringexpense.dto.CreateRecurringExpenseRequest;
import com.tracker.finance.modules.recurringexpense.dto.RecurringExpenseDto;

import java.util.List;
import java.util.UUID;

public interface RecurringExpenseService {
    RecurringExpenseDto addRecurringExpense(CreateRecurringExpenseRequest request, String username);

    List<RecurringExpenseDto> findRecurringExpensesByUser(String username);

    void deleteRecurringExpense(UUID id, String username);
}
