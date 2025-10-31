package org.example.onlineshop.cart.mapper;

import org.example.onlineshop.cart.dto.CartItemRequestDTO;
import org.example.onlineshop.cart.dto.CartItemResponseDTO;
import org.example.onlineshop.cart.model.Cart;
import org.example.onlineshop.cart.model.CartItem;
import org.example.onlineshop.product.model.Product;

public class CartItemMapper {
    public static CartItem toEntity(CartItemRequestDTO dto, Cart cart, Product product) {
        CartItem item = new CartItem();
        item.setCart(cart);
        item.setProduct(product);
        item.setQuantity(dto.getQuantity());
        return item;
    }

    public static CartItemResponseDTO toDTO(CartItem item) {
        CartItemResponseDTO dto = new CartItemResponseDTO();
        dto.setProductId(item.getProduct().getId());
        dto.setProductName(item.getProduct().getTitle());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getProduct().getPrice());
        return dto;
    }
}
