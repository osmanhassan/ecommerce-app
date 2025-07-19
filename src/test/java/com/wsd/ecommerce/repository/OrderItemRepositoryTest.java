package com.wsd.ecommerce.repository;

import com.wsd.ecommerce.entity.Customer;
import com.wsd.ecommerce.entity.Order;
import com.wsd.ecommerce.entity.OrderItem;
import com.wsd.ecommerce.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class OrderItemRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Test
    void testFindTopProductsAllTime() {
        // Setup customer
        Customer customer = new Customer();
        customer.setName("John Doe");
        entityManager.persist(customer);

        // Setup product
        Product product = new Product();
        product.setName("Product A");
        product.setPrice(BigDecimal.valueOf(100));
        entityManager.persist(product);

        // Setup order
        Order order = new Order();
        order.setOrderDate(LocalDate.now());
        order.setTotalAmount(BigDecimal.valueOf(1000));
        order.setCustomer(customer); // ✅ Fix
        entityManager.persist(order);

        // Setup order item
        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setProduct(product);
        item.setPrice(BigDecimal.valueOf(100));
        item.setQuantity(10);
        entityManager.persist(item);

        entityManager.flush();

        List<Object[]> results = orderItemRepository.findTopProductsAllTime(null);
        assertFalse(results.isEmpty());
        assertEquals("Product A", results.get(0)[0]);
        assertTrue(
                BigDecimal.valueOf(1000).compareTo((BigDecimal) results.get(0)[1]) == 0,
                () -> "Expected 1000 but was " + results.get(0)[1]
        );
        assertEquals(10L, results.get(0)[2]); // totalQuantity
    }

    @Test
    void testFindTopProductsLastMonth() {
        // Setup customer
        Customer customer = new Customer();
        customer.setName("Jane Smith");
        entityManager.persist(customer);

        // Setup product
        Product product = new Product();
        product.setName("Product B");
        product.setPrice(BigDecimal.valueOf(50));
        entityManager.persist(product);

        // Setup order
        LocalDate lastMonthDate = LocalDate.now().minusDays(15);
        Order order = new Order();
        order.setOrderDate(lastMonthDate);
        order.setTotalAmount(BigDecimal.valueOf(500));
        order.setCustomer(customer); // ✅ Fix
        entityManager.persist(order);

        // Setup order item
        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setProduct(product);
        item.setPrice(BigDecimal.valueOf(50));
        item.setQuantity(10);
        entityManager.persist(item);

        entityManager.flush();

        List<Object[]> results = orderItemRepository.findTopProductsLastMonth(
                lastMonthDate.minusDays(10),
                LocalDate.now(),
                null
        );
        assertFalse(results.isEmpty());
        assertEquals("Product B", results.get(0)[0]);
    }
}
