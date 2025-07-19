package com.wsd.ecommerce.repository;

import com.wsd.ecommerce.entity.Customer;
import com.wsd.ecommerce.entity.Product;
import com.wsd.ecommerce.entity.Wishlist;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE) // optional, uses real DB if configured
class WishlistRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private WishlistRepository wishlistRepository;


    @Test
    void testFindByCustomer_returnsWishlistItems() {
        // Create and persist customer
        Customer customer = new Customer();
        customer.setName("Test Customer");
        customer = entityManager.persistAndFlush(customer);

        // Create and persist product
        Product product = new Product();
        product.setName("Test Product");
        product.setPrice(BigDecimal.valueOf(150));
        product = entityManager.persistAndFlush(product);

        // Create and persist wishlist
        Wishlist wishlist = new Wishlist();
        wishlist.setCustomer(customer);
        wishlist.setProduct(product);
        entityManager.persistAndFlush(wishlist);

        // Call repository method
        List<Wishlist> wishlistItems = wishlistRepository.findByCustomer(customer);

        // Assertions
        assertNotNull(wishlistItems);
        assertFalse(wishlistItems.isEmpty());
        assertEquals(1, wishlistItems.size());

        Wishlist item = wishlistItems.get(0);
        assertEquals(customer.getId(), item.getCustomer().getId());
        assertEquals(product.getId(), item.getProduct().getId());
        assertEquals("Test Product", item.getProduct().getName());
        assertEquals(0, BigDecimal.valueOf(150).compareTo(item.getProduct().getPrice()));
    }
}
