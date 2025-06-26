package com.tracker.finance.modules.analytics.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class ForecastDto {
    private String title;
    private BigDecimal amount;
    private String category;
    private LocalDate projectedDate;
}