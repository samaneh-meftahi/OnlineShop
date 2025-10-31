package org.example.onlineshop.cart.mapper;

import org.example.onlineshop.cart.model.Cart;
import org.example.onlineshop.cart.model.CartItem;
import org.example.onlineshop.cart.dto.CartItemRequestDTO;
import org.example.onlineshop.cart.dto.CartRequestDTO;
import org.example.onlineshop.cart.dto.CartResponseDTO;
import org.example.onlineshop.product.model.Product;
import org.example.onlineshop.user.model.User;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CartMapper {

    public static CartResponseDTO toDTO(Cart cart) {
        CartResponseDTO dto = new CartResponseDTO();
        dto.setId(cart.getId());
        dto.setUserId(cart.getUser().getId());
        dto.setCreatedAt(cart.getCreatedAt());
        dto.setCartItems(
                cart.getCartItems().stream()
                        .map(CartItemMapper::toDTO)
                        .collect(Collectors.toSet())
        );
        return dto;
    }

    public static Cart toEntity(CartRequestDTO dto, User user, List<Product> products) {
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setCreatedAt(LocalDateTime.now());

        Set<CartItem> cartItems = new HashSet<>();

        for (CartItemRequestDTO itemDTO : dto.getCartItems()) {
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setQuantity(itemDTO.getQuantity());

            Product product = products.stream()
                    .filter(p -> p.getId().equals(itemDTO.getProductId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + itemDTO.getProductId()));

            cartItem.setProduct(product);
            cartItems.add(cartItem);
        }

        cart.setCartItems(cartItems);
        return cart;
    }
}
