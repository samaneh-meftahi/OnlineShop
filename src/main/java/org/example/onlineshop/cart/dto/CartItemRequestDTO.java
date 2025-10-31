package org.example.onlineshop.cart.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemRequestDTO {
    private Long cartId;
    private Long productId;
    private int quantity;
}
