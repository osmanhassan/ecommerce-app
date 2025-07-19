package com.wsd.ecommerce.service;


import com.wsd.ecommerce.dto.ProductDTO;

import java.util.List;

public interface WishlistService {

    List<ProductDTO> getWishlistByCustomerId(Long customerId);
}
