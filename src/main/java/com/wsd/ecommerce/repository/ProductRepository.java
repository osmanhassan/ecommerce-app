package com.wsd.ecommerce.repository;

import com.wsd.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product, Long> {
}
