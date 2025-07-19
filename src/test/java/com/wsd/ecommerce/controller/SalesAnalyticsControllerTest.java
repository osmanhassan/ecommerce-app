package com.wsd.ecommerce.controller;

import com.wsd.ecommerce.controller.api.v1.SalesAnalyticsController;
import com.wsd.ecommerce.dto.TopProductDTO;
import com.wsd.ecommerce.service.SalesAnalyticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SalesAnalyticsController.class)
class SalesAnalyticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SalesAnalyticsService salesService;

    @BeforeEach
    void setUp() {
        Mockito.reset(salesService);
    }

    @Test
    void testGetTodayTotalSales() throws Exception {
        Mockito.when(salesService.getTodayTotalSales()).thenReturn(BigDecimal.valueOf(500));

        mockMvc.perform(get("/api/v1/sales/today"))
                .andExpect(status().isOk())
                .andExpect(content().string("500"));
    }

    @Test
    void testGetMaxSaleDay() throws Exception {
        Mockito.when(salesService.getMaxSaleDay(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn("2025-07-05");

        mockMvc.perform(get("/api/v1/sales/max-day")
                        .param("startDate", "2025-07-01")
                        .param("endDate", "2025-07-10"))
                .andExpect(status().isOk())
                .andExpect(content().string("2025-07-05"));
    }

    @Test
    void testGetMaxSaleDay_InvalidRange_ShouldReturnBadRequest() throws Exception {
        Mockito.doThrow(new IllegalArgumentException("Start date cannot be after end date"))
                .when(salesService).getMaxSaleDay(any(LocalDate.class), any(LocalDate.class));

        mockMvc.perform(get("/api/v1/sales/max-day")
                        .param("startDate", "2025-07-10")
                        .param("endDate", "2025-07-01"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Start date cannot be after end date"));
    }

    @Test
    void testGetTopSellingAllTime() throws Exception {
        Mockito.when(salesService.getTopSellingProductsAllTime(5))
                .thenReturn(List.of(new TopProductDTO("Product A", BigDecimal.valueOf(1500), 30)));

        mockMvc.perform(get("/api/v1/sales/top/all-time"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Product A"));
    }

    @Test
    void testGetTopSellingLastMonth() throws Exception {
        Mockito.when(salesService.getTopSellingProductsLastMonth(5))
                .thenReturn(List.of(new TopProductDTO("Product B", BigDecimal.valueOf(1200), 20)));

        mockMvc.perform(get("/api/v1/sales/top/last-month"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Product B"));
    }

    @Test
    void testGetTopSellingAllTime_EmptyList() throws Exception {
        Mockito.when(salesService.getTopSellingProductsAllTime(5)).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/sales/top/all-time"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    void testGetTopSellingLastMonth_ThrowsException() throws Exception {
        Mockito.when(salesService.getTopSellingProductsLastMonth(5))
                .thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/api/v1/sales/top/last-month"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").value("Unexpected error"));
    }

}

