package com.tracker.finance.modules.budget.service.impl;

import com.tracker.finance.core.exception.specific_exceptions.BadRequestException;
import com.tracker.finance.core.exception.specific_exceptions.ResourceNotFoundException;
import com.tracker.finance.modules.budget.dto.BudgetDto;
import com.tracker.finance.modules.budget.dto.CreateBudgetRequest;
import com.tracker.finance.modules.budget.mapper.BudgetMapper;
import com.tracker.finance.modules.budget.Budget;
import com.tracker.finance.modules.budget.BudgetRepository;
import com.tracker.finance.modules.budget.service.BudgetService;
import com.tracker.finance.modules.user.User;
import com.tracker.finance.modules.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;
    private final BudgetMapper budgetMapper;

    @Override
    public BudgetDto addBudget(CreateBudgetRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        LocalDate monthStart = request.getMonth().atDay(1);
        budgetRepository.findByUser_UsernameAndCategoryAndMonth(username, request.getCategory(), monthStart)
                .ifPresent(b -> {
                    throw new BadRequestException("Budget already exists for this month and category");
                });

        Budget budget = budgetMapper.fromRequest(request);
        budget.setUser(user);
        budget.setMonth(monthStart);
        return budgetMapper.toDto(budgetRepository.save(budget));
    }

    @Override
    public List<BudgetDto> findBudgetsByUser(String username) {
        return budgetRepository.findByUser_Username(username).stream()
                .map(budgetMapper::toDto)
                .collect(Collectors.toList());
    }
}
