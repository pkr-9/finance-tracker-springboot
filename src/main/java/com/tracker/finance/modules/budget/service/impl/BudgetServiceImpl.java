package com.tracker.finance.modules.budget.service.impl;

import com.tracker.finance.core.exception.specific_exceptions.BadRequestException;
import com.tracker.finance.core.exception.specific_exceptions.ResourceNotFoundException;
import com.tracker.finance.core.security.exception.AccessDeniedException;
import com.tracker.finance.modules.budget.dto.BudgetDto;
import com.tracker.finance.modules.budget.dto.CreateBudgetRequest;
import com.tracker.finance.modules.budget.mapper.BudgetMapper;
import com.tracker.finance.modules.budget.Budget;
import com.tracker.finance.modules.budget.BudgetRepository;
import com.tracker.finance.modules.budget.service.BudgetService;
import com.tracker.finance.modules.transaction.Transaction;
import com.tracker.finance.modules.transaction.TransactionRepository;
import com.tracker.finance.modules.user.User;
import com.tracker.finance.modules.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;
    private final BudgetMapper budgetMapper;
    private final TransactionRepository transactionRepository; // Inject TransactionRepository

    @Override
    @Transactional
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

        // When creating, spent is zero
        BudgetDto dto = budgetMapper.toDto(budgetRepository.save(budget));
        dto.setSpent(BigDecimal.ZERO);
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BudgetDto> findBudgetsByUser(String username) {
        // Fetch all budgets for the user
        List<Budget> budgets = budgetRepository.findByUser_Username(username);
        // Fetch all transactions for the user
        List<Transaction> transactions = transactionRepository.findByUser_UsernameOrderByDateDesc(username);

        // Group expenses by category and month (YearMonth)
        Map<String, Map<YearMonth, BigDecimal>> expensesByCategoryAndMonth = transactions.stream()
                .filter(t -> t.getType() == Transaction.TransactionType.EXPENSE)
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.groupingBy(
                                t -> YearMonth.from(t.getDate()),
                                Collectors.mapping(
                                        Transaction::getAmount,
                                        Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)))));

        // Map Budget entities to BudgetDto and set the calculated spent amount
        return budgets.stream().map(budget -> {
            BudgetDto dto = budgetMapper.toDto(budget);
            YearMonth budgetMonth = YearMonth.from(budget.getMonth());
            BigDecimal spent = expensesByCategoryAndMonth
                    .getOrDefault(budget.getCategory(), Map.of())
                    .getOrDefault(budgetMonth, BigDecimal.ZERO);
            dto.setSpent(spent);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BudgetDto updateBudget(UUID budgetId, CreateBudgetRequest request, String username) {
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found with id: " + budgetId));

        if (!budget.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("User is not authorized to update this budget.");
        }

        // Update fields from the request
        budget.setCategory(request.getCategory());
        budget.setLimit_amount(request.getLimitAmount());
        budget.setMonth(request.getMonth().atDay(1));

        Budget updatedBudget = budgetRepository.save(budget);
        return findBudgetsByUser(username).stream()
                .filter(b -> b.getId().equals(updatedBudget.getId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Could not find updated budget details."));
    }

    @Override
    @Transactional
    public void deleteBudget(UUID budgetId, String username) {
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found with id: " + budgetId));

        if (!budget.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("User is not authorized to delete this budget.");
        }
        budgetRepository.delete(budget);
    }
}
