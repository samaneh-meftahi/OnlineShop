package org.example.onlineshop.cart.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.onlineshop.cart.dto.CartItemResponseDTO;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class CartResponseDTO {
    private Long id;
    private Long userId;
    private Set<CartItemResponseDTO> cartItems;
    private LocalDateTime createdAt;
}
