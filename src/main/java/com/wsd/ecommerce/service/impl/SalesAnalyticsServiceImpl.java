package com.wsd.ecommerce.service.impl;

import com.wsd.ecommerce.dto.TopProductDTO;
import com.wsd.ecommerce.helper.DateRangeHelper;
import com.wsd.ecommerce.helper.TopProductMapperHelper;
import com.wsd.ecommerce.repository.OrderItemRepository;
import com.wsd.ecommerce.repository.OrderRepository;
import com.wsd.ecommerce.service.SalesAnalyticsService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class SalesAnalyticsServiceImpl implements SalesAnalyticsService {

    private static final Logger logger = LoggerFactory.getLogger(SalesAnalyticsServiceImpl.class);

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public SalesAnalyticsServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public BigDecimal getTodayTotalSales() {
        LocalDate today = LocalDate.now();
        logger.info("Calculating total sales for today: {}", today);
        BigDecimal totalSales = orderRepository.getTotalSalesForDay(today);
        logger.debug("Total sales amount for {} is {}", today, totalSales);
        return totalSales;
    }

    @Override
    public String getMaxSaleDay(LocalDate start, LocalDate end) {
        logger.info("Finding max sale day between {} and {}", start, end);
        if (start.isAfter(end)) {
            logger.error("Invalid date range: start {} is after end {}", start, end);
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        List<Object[]> results = orderRepository.findMaxSaleDayInRange(start, end);
        if (results.isEmpty()) {
            logger.warn("No sales found in the range {} to {}", start, end);
            return "No sales in given range";
        }
        Object[] maxSale = results.get(0);
        LocalDate maxSaleDay = (LocalDate) maxSale[0];
        BigDecimal maxAmount = (BigDecimal) maxSale[1];
        logger.info("Max sale day is {} with amount {}", maxSaleDay, maxAmount);
        return maxSaleDay.toString();
    }

    @Override
    public List<TopProductDTO> getTopSellingProductsAllTime(int limit) {
        logger.info("Fetching top {} selling products of all time", limit);
        Pageable pageable = PageRequest.of(0, limit);
        List<Object[]> results = orderItemRepository.findTopProductsAllTime(pageable);
        return TopProductMapperHelper.mapResultsToDTO(results, limit);
    }

    @Override
    public List<TopProductDTO> getTopSellingProductsLastMonth(int limit) {
        LocalDate startDate = DateRangeHelper.getLastMonthStart();
        LocalDate endDate = DateRangeHelper.getLastMonthEnd();
        logger.info("Fetching top {} selling products from {} to {}", limit, startDate, endDate);

        Pageable pageable = PageRequest.of(0, limit);
        List<Object[]> results = orderItemRepository.findTopProductsLastMonth(startDate, endDate, pageable);

        return TopProductMapperHelper.mapResultsToDTO(results, limit);
    }
}
