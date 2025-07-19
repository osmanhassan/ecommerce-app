package com.wsd.ecommerce.controller.api.v1;

import com.wsd.ecommerce.dto.TopProductDTO;
import com.wsd.ecommerce.exception.ApiError;
import com.wsd.ecommerce.service.SalesAnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/sales")
@Tag(name = "Sales API", description = "Endpoints for sales analytics")
public class SalesAnalyticsController {

    private final SalesAnalyticsService salesService;

    public SalesAnalyticsController(SalesAnalyticsService salesService) {
        this.salesService = salesService;
    }

    @GetMapping("/today")
    @Operation(summary = "Get today's total sales amount")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully fetched today's total sales"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<BigDecimal> getTodayTotalSales() {
        return ResponseEntity.ok(salesService.getTodayTotalSales());
    }

    @GetMapping("/max-day")
    @Operation(summary = "Get the day with max sales in given range")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully fetched max sale day"),
            @ApiResponse(responseCode = "400", description = "Invalid date range",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<String> getMaxSaleDay(
            @Parameter(description = "Start date (yyyy-MM-dd)", example = "2025-07-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date (yyyy-MM-dd)", example = "2025-07-18")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return ResponseEntity.ok(salesService.getMaxSaleDay(startDate, endDate));
    }

    @GetMapping("/top/all-time")
    @Operation(summary = "Get top 5 selling products of all time by sales amount")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully fetched top products"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<List<TopProductDTO>> getTopSellingAllTime() {
        return ResponseEntity.ok(salesService.getTopSellingProductsAllTime(5));
    }

    @GetMapping("/top/last-month")
    @Operation(summary = "Get top 5 selling products from last month by sales count")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully fetched top products"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<List<TopProductDTO>> getTopSellingLastMonth() {
        return ResponseEntity.ok(salesService.getTopSellingProductsLastMonth(5));
    }
}
