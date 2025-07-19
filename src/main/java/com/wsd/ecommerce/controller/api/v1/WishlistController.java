package com.wsd.ecommerce.controller.api.v1;

import com.wsd.ecommerce.dto.ProductDTO;
import com.wsd.ecommerce.exception.ApiError;
import com.wsd.ecommerce.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/wishlist")
@Tag(name = "Wishlist API", description = "Operations related to customer's wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping("/{customerId}")
    @Operation(summary = "Get wishlist items of a customer by customer ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved wishlist"),
            @ApiResponse(responseCode = "404", description = "Customer not found",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<List<ProductDTO>> getWishlistByCustomer(
            @Parameter(description = "Customer ID", example = "1") @PathVariable Long customerId) {

        List<ProductDTO> wishlist = wishlistService.getWishlistByCustomerId(customerId);
        return ResponseEntity.ok(wishlist);
    }
}
