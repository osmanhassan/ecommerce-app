package com.wsd.ecommerce.repository;

import com.wsd.ecommerce.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Override
    Optional<Customer> findById(Long id);
}