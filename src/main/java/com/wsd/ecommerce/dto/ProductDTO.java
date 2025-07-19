package com.wsd.ecommerce.dto;

import java.math.BigDecimal;

public class ProductDTO {
    private Long id;
    private String name;
    private BigDecimal price;

    public ProductDTO(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }
}
