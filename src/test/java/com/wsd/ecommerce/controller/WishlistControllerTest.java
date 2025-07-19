package com.wsd.ecommerce.controller;

import com.wsd.ecommerce.controller.api.v1.WishlistController;
import com.wsd.ecommerce.dto.ProductDTO;
import com.wsd.ecommerce.service.WishlistService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WishlistController.class)
class WishlistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WishlistService wishlistService;

    @Test
    void getWishlistByCustomer_shouldReturnProductDTOList() throws Exception {
        // Arrange: prepare mock response
        List<ProductDTO> products = List.of(
                new ProductDTO(101L, "Product A", BigDecimal.valueOf(1000)),
                new ProductDTO(102L, "Product B", BigDecimal.valueOf(1200))
        );

        Mockito.when(wishlistService.getWishlistByCustomerId(anyLong()))
                .thenReturn(products);

        // Act & Assert: perform GET and verify JSON response
        mockMvc.perform(get("/api/v1/wishlist/{customerId}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(products.size()))
                .andExpect(jsonPath("$[0].id").value(101))
                .andExpect(jsonPath("$[0].name").value("Product A"))
                .andExpect(jsonPath("$[0].price").value(1000))
                .andExpect(jsonPath("$[1].id").value(102))
                .andExpect(jsonPath("$[1].name").value("Product B"))
                .andExpect(jsonPath("$[1].price").value(1200));
    }
}
