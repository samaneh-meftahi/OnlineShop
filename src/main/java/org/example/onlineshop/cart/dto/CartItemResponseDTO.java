package org.example.onlineshop.cart.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class CartItemResponseDTO {
    private Long productId;
    private String productName;
    private int quantity;
    private BigDecimal price;

}
