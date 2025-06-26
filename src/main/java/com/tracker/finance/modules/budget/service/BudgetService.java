package com.tracker.finance.modules.budget.service;

import com.tracker.finance.modules.budget.dto.BudgetDto;
import com.tracker.finance.modules.budget.dto.CreateBudgetRequest;

import java.util.List;

public interface BudgetService {
    BudgetDto addBudget(CreateBudgetRequest request, String username);

    List<BudgetDto> findBudgetsByUser(String username);
}
