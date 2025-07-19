package com.wsd.ecommerce.repository;

import com.wsd.ecommerce.entity.Customer;
import com.wsd.ecommerce.entity.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void testGetTotalSalesForDay() {
        LocalDate today = LocalDate.now();

        // Create and persist customer
        Customer customer = new Customer();
        customer.setName("Test Customer");
        entityManager.persist(customer);

        Order order1 = new Order();
        order1.setOrderDate(today);
        order1.setTotalAmount(BigDecimal.valueOf(500));
        order1.setCustomer(customer);  // Set customer here
        entityManager.persist(order1);

        Order order2 = new Order();
        order2.setOrderDate(today);
        order2.setTotalAmount(BigDecimal.valueOf(300));
        order2.setCustomer(customer);  // Set customer here
        entityManager.persist(order2);

        entityManager.flush();

        BigDecimal totalSales = orderRepository.getTotalSalesForDay(today);
        assertEquals(0, totalSales.compareTo(BigDecimal.valueOf(800)));
    }

    @Test
    void testFindMaxSaleDayInRange() {
        LocalDate day1 = LocalDate.now().minusDays(3);
        LocalDate day2 = LocalDate.now().minusDays(2);

        Customer customer = new Customer();
        customer.setName("Test Customer");
        entityManager.persist(customer);

        Order order1 = new Order();
        order1.setOrderDate(day1);
        order1.setTotalAmount(BigDecimal.valueOf(200));
        order1.setCustomer(customer);  // Set customer here
        entityManager.persist(order1);

        Order order2 = new Order();
        order2.setOrderDate(day2);
        order2.setTotalAmount(BigDecimal.valueOf(1000));
        order2.setCustomer(customer);  // Set customer here
        entityManager.persist(order2);

        entityManager.flush();

        List<Object[]> result = orderRepository.findMaxSaleDayInRange(day1.minusDays(1), LocalDate.now());
        assertFalse(result.isEmpty());
        assertEquals(day2, result.get(0)[0]);
        assertEquals(0, ((BigDecimal) result.get(0)[1]).compareTo(BigDecimal.valueOf(1000)));
    }

}

