package com.wsd.ecommerce.repository;

import com.wsd.ecommerce.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.orderDate = :orderDate")
    BigDecimal getTotalSalesForDay(@Param("orderDate") LocalDate orderDate);

    @Query("SELECT o.orderDate, SUM(o.totalAmount) FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate GROUP BY o.orderDate ORDER BY SUM(o.totalAmount) DESC")
    List<Object[]> findMaxSaleDayInRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
