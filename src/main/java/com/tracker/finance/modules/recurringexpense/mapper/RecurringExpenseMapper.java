package com.tracker.finance.modules.recurringexpense.mapper;

import com.tracker.finance.modules.recurringexpense.RecurringExpense;
import com.tracker.finance.modules.recurringexpense.dto.CreateRecurringExpenseRequest;
import com.tracker.finance.modules.recurringexpense.dto.RecurringExpenseDto;
import org.springframework.stereotype.Component;

@Component
public class RecurringExpenseMapper {

    public RecurringExpenseDto toDto(RecurringExpense expense) {
        if (expense == null) {
            return null;
        }
        return RecurringExpenseDto.builder()
                .id(expense.getId())
                .title(expense.getTitle())
                .amount(expense.getAmount())
                .frequency(expense.getFrequency())
                .dayOfPeriod(expense.getDayOfPeriod()) // Added mapping
                .category(expense.getCategory())
                .build();
    }

    public RecurringExpense fromRequest(CreateRecurringExpenseRequest request) {
        if (request == null) {
            return null;
        }
        RecurringExpense expense = new RecurringExpense();
        expense.setTitle(request.getTitle());
        expense.setAmount(request.getAmount());
        expense.setFrequency(request.getFrequency());
        expense.setDayOfPeriod(request.getDayOfPeriod()); // Added mapping
        expense.setCategory(request.getCategory());
        return expense;
    }
}