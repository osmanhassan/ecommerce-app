package com.wsd.ecommerce.repository;

import com.wsd.ecommerce.entity.OrderItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // For all time top 5 products by total sales amount (price * quantity)
    @Query("SELECT oi.product.name, SUM(oi.price * oi.quantity) as totalAmount, SUM(oi.quantity) as totalQuantity " +
            "FROM OrderItem oi GROUP BY oi.product.name ORDER BY totalAmount DESC")
    List<Object[]> findTopProductsAllTime(Pageable pageable);

    // For last month top 5 products by number of sales (quantity)
    @Query("SELECT oi.product.name, SUM(oi.price * oi.quantity) as totalAmount, SUM(oi.quantity) as totalQuantity " +
            "FROM OrderItem oi WHERE oi.order.orderDate BETWEEN :startDate AND :endDate " +
            "GROUP BY oi.product.name ORDER BY totalQuantity DESC")
    List<Object[]> findTopProductsLastMonth(LocalDate startDate, LocalDate endDate, Pageable pageable);
}
