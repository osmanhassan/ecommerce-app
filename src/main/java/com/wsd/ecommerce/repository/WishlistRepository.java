package com.wsd.ecommerce.repository;

import com.wsd.ecommerce.entity.Customer;
import com.wsd.ecommerce.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findByCustomer(Customer customer);
}
