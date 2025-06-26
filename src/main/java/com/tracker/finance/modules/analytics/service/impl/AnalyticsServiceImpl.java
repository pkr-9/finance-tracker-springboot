package com.tracker.finance.modules.analytics.service.impl;

import com.tracker.finance.modules.analytics.dto.ForecastDto;
import com.tracker.finance.modules.analytics.dto.MonthlySummaryDto;
import com.tracker.finance.modules.analytics.service.AnalyticsService;
import com.tracker.finance.modules.budget.Budget;
import com.tracker.finance.modules.budget.BudgetRepository;
import com.tracker.finance.modules.recurringexpense.RecurringExpense;
import com.tracker.finance.modules.recurringexpense.RecurringExpenseRepository;
import com.tracker.finance.modules.transaction.Transaction;
import com.tracker.finance.modules.transaction.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

        private final TransactionRepository transactionRepository;
        private final BudgetRepository budgetRepository;
        private final RecurringExpenseRepository recurringExpenseRepository;

        @Override
        @Transactional(readOnly = true)
        public MonthlySummaryDto getMonthlySummary(YearMonth month, String username) {
                LocalDate startDate = month.atDay(1);
                LocalDate endDate = month.atEndOfMonth();

                List<Transaction> transactions = transactionRepository.findByUser_UsernameAndDateBetween(
                                username, startDate, endDate);
                BigDecimal totalIncome = calculateTotal(transactions, Transaction.TransactionType.INCOME);
                BigDecimal totalExpenses = calculateTotal(transactions, Transaction.TransactionType.EXPENSE);
                Map<String, BigDecimal> expensesByCategory = transactions.stream()
                                .filter(t -> t.getType() == Transaction.TransactionType.EXPENSE)
                                .collect(Collectors.groupingBy(
                                                Transaction::getCategory,
                                                Collectors.mapping(Transaction::getAmount,
                                                                Collectors.reducing(BigDecimal.ZERO,
                                                                                BigDecimal::add))));
                Map<String, MonthlySummaryDto.BudgetStatus> budgetStatus = checkBudgets(username, startDate,
                                expensesByCategory);
                return MonthlySummaryDto.builder()
                                .totalIncome(totalIncome)
                                .totalExpenses(totalExpenses)
                                .savings(totalIncome.subtract(totalExpenses))
                                .expensesByCategory(expensesByCategory)
                                .budgetStatus(budgetStatus)
                                .build();
        }

        private BigDecimal calculateTotal(List<Transaction> transactions, Transaction.TransactionType type) {
                return transactions.stream()
                                .filter(t -> t.getType() == type)
                                .map(Transaction::getAmount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        private Map<String, MonthlySummaryDto.BudgetStatus> checkBudgets(String username, LocalDate monthStart,
                        Map<String, BigDecimal> expensesByCategory) {
                List<Budget> budgets = budgetRepository.findByUserAndMonth(username, monthStart);
                return budgets.stream()
                                .collect(Collectors.toMap(
                                                Budget::getCategory,
                                                budget -> {
                                                        BigDecimal spent = expensesByCategory.getOrDefault(
                                                                        budget.getCategory(), BigDecimal.ZERO);
                                                        BigDecimal budgetLimit = budget.getLimit_amount();
                                                        return MonthlySummaryDto.BudgetStatus.builder()
                                                                        .spent(spent)
                                                                        .budgetLimit(budgetLimit)
                                                                        .remaining(budgetLimit.subtract(spent))
                                                                        .build();
                                                }));
        }

        @Override
        @Transactional(readOnly = true)
        public List<ForecastDto> getExpenseForecast(String username) {
                List<RecurringExpense> recurringExpenses = recurringExpenseRepository.findByUser_Username(username);
                List<ForecastDto> forecast = new ArrayList<>();
                LocalDate nextMonth = LocalDate.now().plusMonths(1);

                for (RecurringExpense expense : recurringExpenses) {
                        if (expense.getFrequency() == RecurringExpense.Frequency.MONTHLY) {
                                // For monthly, project to the specified day of next month.
                                int day = Math.min(expense.getDayOfPeriod(), nextMonth.lengthOfMonth());
                                forecast.add(createForecastDto(expense, nextMonth.withDayOfMonth(day)));
                        } else if (expense.getFrequency() == RecurringExpense.Frequency.WEEKLY) {
                                // For weekly, find all matching weekdays in the next month.
                                LocalDate startOfNextMonth = nextMonth.with(TemporalAdjusters.firstDayOfMonth());
                                LocalDate endOfNextMonth = nextMonth.with(TemporalAdjusters.lastDayOfMonth());

                                // Find the first occurrence of the target day in the next month
                                DayOfWeek targetDayOfWeek = DayOfWeek.of(expense.getDayOfPeriod());
                                LocalDate date = startOfNextMonth.with(TemporalAdjusters.nextOrSame(targetDayOfWeek));

                                while (!date.isAfter(endOfNextMonth)) {
                                        forecast.add(createForecastDto(expense, date));
                                        date = date.plusWeeks(1);
                                }
                        }
                }

                return forecast;
        }

        private ForecastDto createForecastDto(RecurringExpense expense, LocalDate projectedDate) {
                return ForecastDto.builder()
                                .title(expense.getTitle())
                                .amount(expense.getAmount())
                                .category(expense.getCategory())
                                .projectedDate(projectedDate)
                                .build();
        }
}