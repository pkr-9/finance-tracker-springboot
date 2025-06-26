package com.tracker.finance.modules.recurringexpense.controller;

import com.tracker.finance.modules.recurringexpense.dto.CreateRecurringExpenseRequest;
import com.tracker.finance.modules.recurringexpense.dto.RecurringExpenseDto;
import com.tracker.finance.modules.recurringexpense.service.RecurringExpenseService;

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
@RequestMapping("/api/recurring-expenses")
@RequiredArgsConstructor
public class RecurringExpenseController {

    private final RecurringExpenseService recurringExpenseService;

    @PostMapping
    public ResponseEntity<RecurringExpenseDto> addRecurringExpense(
            @Valid @RequestBody CreateRecurringExpenseRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        RecurringExpenseDto newExpense = recurringExpenseService.addRecurringExpense(request,
                userDetails.getUsername());
        return new ResponseEntity<>(newExpense, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RecurringExpenseDto>> getRecurringExpenses(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(recurringExpenseService.findRecurringExpensesByUser(userDetails.getUsername()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecurringExpense(@PathVariable UUID id,
            @AuthenticationPrincipal UserDetails userDetails) {
        recurringExpenseService.deleteRecurringExpense(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}