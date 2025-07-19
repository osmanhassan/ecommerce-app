package com.wsd.ecommerce.helper;

import com.wsd.ecommerce.dto.TopProductDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TopProductMapperHelper {

    public static List<TopProductDTO> mapResultsToDTO(List<Object[]> results, int limit) {
        List<TopProductDTO> topProducts = new ArrayList<>();
        int size = Math.min(results.size(), limit);

        for (int i = 0; i < size; i++) {
            Object[] row = results.get(i);
            String productName = (String) row[0];
            BigDecimal totalSales = (BigDecimal) row[1];
            long totalQuantity = ((Number) row[2]).longValue();
            topProducts.add(new TopProductDTO(productName, totalSales, totalQuantity));
        }
        return topProducts;
    }
}

