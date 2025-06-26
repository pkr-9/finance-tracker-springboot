package com.tracker.finance.modules.budget.mapper;

import com.tracker.finance.modules.budget.Budget;
import com.tracker.finance.modules.budget.dto.BudgetDto;
import com.tracker.finance.modules.budget.dto.CreateBudgetRequest;
import org.springframework.stereotype.Component;

@Component
public class BudgetMapper {
    public BudgetDto toDto(Budget b) {
        return BudgetDto.builder()
                .id(b.getId())
                .category(b.getCategory())
                .limitAmount(b.getLimit_amount())
                .month(b.getMonth())
                .build();
    }

    public Budget fromRequest(CreateBudgetRequest request) {
        Budget b = new Budget();
        b.setCategory(request.getCategory());
        b.setLimit_amount(request.getLimitAmount());
        return b;
    }
}
