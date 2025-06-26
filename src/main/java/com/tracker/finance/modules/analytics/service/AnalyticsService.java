package com.tracker.finance.modules.analytics.service;

import com.tracker.finance.modules.analytics.dto.ForecastDto;
import com.tracker.finance.modules.analytics.dto.MonthlySummaryDto;

import java.time.YearMonth;
import java.util.List;

public interface AnalyticsService {
        MonthlySummaryDto getMonthlySummary(YearMonth month, String username);

        List<ForecastDto> getExpenseForecast(String username);
}
