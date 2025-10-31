package org.example.onlineshop.cart.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.onlineshop.cart.dto.CartItemRequestDTO;

import java.util.Set;

@Getter
@Setter
public class CartRequestDTO {
    private Long userId;
    private Set<CartItemRequestDTO> cartItems;

}
