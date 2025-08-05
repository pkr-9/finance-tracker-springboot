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
import java.util.UUID;

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

    @PutMapping("/{id}")
    public ResponseEntity<BudgetDto> updateBudget(
            @PathVariable("id") UUID id,
            @Valid @RequestBody CreateBudgetRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        BudgetDto updatedBudget = budgetService.updateBudget(id, request, userDetails.getUsername());
        return ResponseEntity.ok(updatedBudget);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBudget(
            @PathVariable("id") UUID id,
            @AuthenticationPrincipal UserDetails userDetails) {
        budgetService.deleteBudget(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}
