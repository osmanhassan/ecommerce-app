package com.wsd.ecommerce.exception;

import com.wsd.ecommerce.controller.api.v1.SalesAnalyticsController;
import com.wsd.ecommerce.service.SalesAnalyticsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SalesAnalyticsController.class)
@Import(GlobalExceptionHandler.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SalesAnalyticsService salesAnalyticsService;

    @Test
    void testHandleIllegalArgument() throws Exception {
        // Mock the service to throw IllegalArgumentException
        when(salesAnalyticsService.getMaxSaleDay(any(), any()))
                .thenThrow(new IllegalArgumentException("Start date cannot be after end date"));

        mockMvc.perform(get("/api/v1/sales/max-day")
                        .param("startDate", "2025-07-10")
                        .param("endDate", "2025-07-01"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400));
    }
}

