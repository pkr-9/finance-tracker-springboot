package com.tracker.finance.modules.budget.service;

import com.tracker.finance.modules.budget.dto.BudgetDto;
import com.tracker.finance.modules.budget.dto.CreateBudgetRequest;

import java.util.List;
import java.util.UUID;

public interface BudgetService {
    BudgetDto addBudget(CreateBudgetRequest request, String username);

    List<BudgetDto> findBudgetsByUser(String username);

    BudgetDto updateBudget(UUID budgetId, CreateBudgetRequest request, String username); // Add this

    void deleteBudget(UUID budgetId, String username); // Add this
}
