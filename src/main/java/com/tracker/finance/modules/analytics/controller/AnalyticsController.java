package com.tracker.finance.modules.analytics.controller;

import com.tracker.finance.modules.analytics.dto.MonthlySummaryDto;
import com.tracker.finance.modules.analytics.service.AnalyticsService;
import com.tracker.finance.modules.analytics.dto.ForecastDto;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/monthly-summary")
    public ResponseEntity<MonthlySummaryDto> getMonthlySummary(
            @RequestParam("month") @DateTimeFormat(pattern = "yyyy-MM") YearMonth month,
            @AuthenticationPrincipal UserDetails userDetails) {
        MonthlySummaryDto summary = analyticsService.getMonthlySummary(month, userDetails.getUsername());
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/forecast")
    public ResponseEntity<List<ForecastDto>> getExpenseForecast(@AuthenticationPrincipal UserDetails userDetails) {
        List<ForecastDto> forecast = analyticsService.getExpenseForecast(userDetails.getUsername());
        return ResponseEntity.ok(forecast);
    }
}