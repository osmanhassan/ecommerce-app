package com.wsd.ecommerce.service.impl;

import com.wsd.ecommerce.dto.ProductDTO;
import com.wsd.ecommerce.entity.Customer;
import com.wsd.ecommerce.exception.CustomerNotFoundException;
import com.wsd.ecommerce.repository.CustomerRepository;
import com.wsd.ecommerce.repository.WishlistRepository;
import com.wsd.ecommerce.service.WishlistService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class WishlistServiceImpl implements WishlistService {

    private static final Logger logger = LoggerFactory.getLogger(WishlistServiceImpl.class);

    private final WishlistRepository wishlistRepository;
    private final CustomerRepository customerRepository;

    public WishlistServiceImpl(WishlistRepository wishlistRepository, CustomerRepository customerRepository) {
        this.wishlistRepository = wishlistRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public List<ProductDTO> getWishlistByCustomerId(Long customerId) {
        logger.info("Fetching wishlist for customer ID: {}", customerId);

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> {
                    logger.error("Customer not found: {}", customerId);
                    return new CustomerNotFoundException(customerId);
                });

        List<ProductDTO> wishlist = wishlistRepository.findByCustomer(customer).stream()
                .map(w -> new ProductDTO(
                        w.getProduct().getId(),
                        w.getProduct().getName(),
                        w.getProduct().getPrice()
                ))
                .collect(Collectors.toList());

        logger.debug("Wishlist for customer {}: {} items", customerId, wishlist.size());
        return wishlist;
    }
}
