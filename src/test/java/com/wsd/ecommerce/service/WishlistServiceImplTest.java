package com.wsd.ecommerce.service;

import com.wsd.ecommerce.dto.ProductDTO;
import com.wsd.ecommerce.entity.Customer;
import com.wsd.ecommerce.entity.Product;
import com.wsd.ecommerce.entity.Wishlist;
import com.wsd.ecommerce.exception.CustomerNotFoundException;
import com.wsd.ecommerce.repository.CustomerRepository;
import com.wsd.ecommerce.repository.WishlistRepository;
import com.wsd.ecommerce.service.impl.WishlistServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WishlistServiceImplTest {

    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private WishlistServiceImpl wishlistService;

    @Test
    void getWishlistByCustomerId_whenCustomerExists_returnsProductDTOList() {
        Long customerId = 1L;

        Customer customer = new Customer();
        customer.setId(customerId);

        Product product1 = new Product();
        product1.setId(101L);
        product1.setName("Product A");
        product1.setPrice(BigDecimal.valueOf(1000));

        Product product2 = new Product();
        product2.setId(102L);
        product2.setName("Product B");
        product2.setPrice(BigDecimal.valueOf(1200));

        Wishlist wishlistItem1 = new Wishlist();
        wishlistItem1.setProduct(product1);
        wishlistItem1.setCustomer(customer);

        Wishlist wishlistItem2 = new Wishlist();
        wishlistItem2.setProduct(product2);
        wishlistItem2.setCustomer(customer);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(wishlistRepository.findByCustomer(customer)).thenReturn(List.of(wishlistItem1, wishlistItem2));

        List<ProductDTO> result = wishlistService.getWishlistByCustomerId(customerId);

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(101L, result.get(0).getId());
        assertEquals("Product A", result.get(0).getName());
        assertEquals(0, BigDecimal.valueOf(1000).compareTo(result.get(0).getPrice()));

        assertEquals(102L, result.get(1).getId());
        assertEquals("Product B", result.get(1).getName());
        assertEquals(0, BigDecimal.valueOf(1200).compareTo(result.get(1).getPrice()));

        verify(customerRepository, times(1)).findById(customerId);
        verify(wishlistRepository, times(1)).findByCustomer(customer);
    }

    @Test
    void getWishlistByCustomerId_whenCustomerNotFound_throwsException() {
        Long customerId = 999L;

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> wishlistService.getWishlistByCustomerId(customerId));

        verify(customerRepository, times(1)).findById(customerId);
        verifyNoInteractions(wishlistRepository);
    }

    @Test
    void testGetWishlistByCustomerId_EmptyWishlist() {
        Long customerId = 1L;
        Customer customer = new Customer();
        customer.setId(customerId);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(wishlistRepository.findByCustomer(customer)).thenReturn(List.of());

        List<ProductDTO> result = wishlistService.getWishlistByCustomerId(customerId);
        assertTrue(result.isEmpty());
    }
}
