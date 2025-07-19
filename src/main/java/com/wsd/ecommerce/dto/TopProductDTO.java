package com.wsd.ecommerce.dto;

import java.math.BigDecimal;

public class TopProductDTO {
    private String name;
    private BigDecimal totalSales;
    private long totalQuantity;

    public TopProductDTO(String name, BigDecimal totalSales, long totalQuantity) {
        this.name = name;
        this.totalSales = totalSales;
        this.totalQuantity = totalQuantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(BigDecimal totalSales) {
        this.totalSales = totalSales;
    }

    public long getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(long totalQuantity) {
        this.totalQuantity = totalQuantity;
    }
// Getters
}

