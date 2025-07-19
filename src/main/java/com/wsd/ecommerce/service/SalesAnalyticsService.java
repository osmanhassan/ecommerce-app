package com.wsd.ecommerce.service;

import com.wsd.ecommerce.dto.TopProductDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface SalesAnalyticsService {
    BigDecimal getTodayTotalSales();
    String getMaxSaleDay(LocalDate start, LocalDate end);
    List<TopProductDTO> getTopSellingProductsAllTime(int limit);
    List<TopProductDTO> getTopSellingProductsLastMonth(int limit);
}

