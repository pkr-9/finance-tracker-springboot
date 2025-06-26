package com.tracker.finance.modules.budget.controller;

import com.tracker.finance.modules.budget.dto.BudgetDto;
import com.tracker.finance.modules.budget.dto.CreateBudgetRequest;
import com.tracker.finance.modules.budget.service.BudgetService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    @GetMapping
    public ResponseEntity<List<BudgetDto>> getBudgets(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(budgetService.findBudgetsByUser(userDetails.getUsername()));
    }

    @PostMapping
    public ResponseEntity<BudgetDto> addBudget(
            @Valid @RequestBody CreateBudgetRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        BudgetDto newBudget = budgetService.addBudget(request, userDetails.getUsername());
        return new ResponseEntity<>(newBudget, HttpStatus.CREATED);
    }
}