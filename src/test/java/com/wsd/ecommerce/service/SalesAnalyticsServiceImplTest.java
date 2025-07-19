package com.wsd.ecommerce.service;

import com.wsd.ecommerce.dto.TopProductDTO;
import com.wsd.ecommerce.repository.OrderItemRepository;
import com.wsd.ecommerce.repository.OrderRepository;
import com.wsd.ecommerce.service.impl.SalesAnalyticsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SalesAnalyticsServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private SalesAnalyticsServiceImpl service;

    @Test
    void testGetTodayTotalSales() {
        LocalDate today = LocalDate.now();
        when(orderRepository.getTotalSalesForDay(today)).thenReturn(BigDecimal.valueOf(1000));

        BigDecimal result = service.getTodayTotalSales();

        assertEquals(BigDecimal.valueOf(1000), result);
        verify(orderRepository, times(1)).getTotalSalesForDay(today);
    }

    @Test
    void testGetMaxSaleDay_valid() {
        LocalDate start = LocalDate.now().minusDays(10);
        LocalDate end = LocalDate.now();
        Object[] maxSale = new Object[]{start.plusDays(5), BigDecimal.valueOf(1234)};
        when(orderRepository.findMaxSaleDayInRange(start, end)).thenReturn(List.<Object[]>of(maxSale));

        String result = service.getMaxSaleDay(start, end);

        assertEquals(start.plusDays(5).toString(), result);
        verify(orderRepository, times(1)).findMaxSaleDayInRange(start, end);
    }

    @Test
    void testGetMaxSaleDay_noSales() {
        LocalDate start = LocalDate.now().minusDays(10);
        LocalDate end = LocalDate.now();
        when(orderRepository.findMaxSaleDayInRange(start, end)).thenReturn(new ArrayList<>());

        String result = service.getMaxSaleDay(start, end);

        assertEquals("No sales in given range", result);
        verify(orderRepository, times(1)).findMaxSaleDayInRange(start, end);
    }

    @Test
    void testGetMaxSaleDay_startAfterEnd_throws() {
        LocalDate start = LocalDate.now();
        LocalDate end = start.minusDays(1);

        assertThrows(IllegalArgumentException.class, () -> service.getMaxSaleDay(start, end));
        verify(orderRepository, never()).findMaxSaleDayInRange(any(), any());
    }

    @Test
    void testGetTopSellingProductsAllTime() {
        int limit = 2;
        Pageable pageable = PageRequest.of(0, limit);
        List<Object[]> mockResults = List.of(
                new Object[]{"Product A", BigDecimal.valueOf(500), 5L},
                new Object[]{"Product B", BigDecimal.valueOf(300), 3L}
        );

        when(orderItemRepository.findTopProductsAllTime(pageable)).thenReturn(mockResults);

        List<TopProductDTO> result = service.getTopSellingProductsAllTime(limit);

        assertEquals(2, result.size());
        assertEquals("Product A", result.get(0).getName());
        assertEquals(BigDecimal.valueOf(500), result.get(0).getTotalSales());
        assertEquals(5L, result.get(0).getTotalQuantity());
        verify(orderItemRepository, times(1)).findTopProductsAllTime(pageable);
    }

    @Test
    void testGetTopSellingProductsLastMonth() {
        int limit = 1;
        LocalDate lastMonthStart = LocalDate.now().minusMonths(1).withDayOfMonth(1);
        LocalDate lastMonthEnd = lastMonthStart.withDayOfMonth(lastMonthStart.lengthOfMonth());

        Pageable pageable = PageRequest.of(0, limit);
        List<Object[]> mockResults = Arrays.asList(
                new Object[][]{new Object[]{"Product X", BigDecimal.valueOf(1000), 10L}}
        );

        when(orderItemRepository.findTopProductsLastMonth(lastMonthStart, lastMonthEnd, pageable))
                .thenReturn(mockResults);

        List<TopProductDTO> result = service.getTopSellingProductsLastMonth(limit);

        assertEquals(1, result.size());
        assertEquals("Product X", result.get(0).getName());
        assertEquals(BigDecimal.valueOf(1000), result.get(0).getTotalSales());
        assertEquals(10L, result.get(0).getTotalQuantity());
        verify(orderItemRepository, times(1))
                .findTopProductsLastMonth(lastMonthStart, lastMonthEnd, pageable);
    }

    @Test
    void testGetTopSellingProductsAllTime_EmptyResults() {
        int limit = 3;
        Pageable pageable = PageRequest.of(0, limit);

        when(orderItemRepository.findTopProductsAllTime(pageable)).thenReturn(new ArrayList<>());

        List<TopProductDTO> result = service.getTopSellingProductsAllTime(limit);

        assertTrue(result.isEmpty());
        verify(orderItemRepository, times(1)).findTopProductsAllTime(pageable);
    }

    @Test
    void testGetTopSellingProductsLastMonth_EmptyResults() {
        int limit = 3;
        LocalDate lastMonthStart = LocalDate.now().minusMonths(1).withDayOfMonth(1);
        LocalDate lastMonthEnd = lastMonthStart.withDayOfMonth(lastMonthStart.lengthOfMonth());

        Pageable pageable = PageRequest.of(0, limit);
        when(orderItemRepository.findTopProductsLastMonth(lastMonthStart, lastMonthEnd, pageable))
                .thenReturn(new ArrayList<>());

        List<TopProductDTO> result = service.getTopSellingProductsLastMonth(limit);

        assertTrue(result.isEmpty());
    }

}
