package com.wsd.ecommerce.helper;

import java.time.LocalDate;
import java.time.YearMonth;

public class DateRangeHelper {

    public static LocalDate getLastMonthStart() {
        return YearMonth.now().minusMonths(1).atDay(1);
    }

    public static LocalDate getLastMonthEnd() {
        return YearMonth.now().atDay(1).minusDays(1); // inclusive end
    }
}

