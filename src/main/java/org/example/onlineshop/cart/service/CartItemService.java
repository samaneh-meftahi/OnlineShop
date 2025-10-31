package org.example.onlineshop.cart.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.onlineshop.cart.dto.CartItemRequestDTO;
import org.example.onlineshop.cart.dto.CartItemResponseDTO;
import org.example.onlineshop.common.exception.InvalidRequestException;
import org.example.onlineshop.common.exception.ResourceNotFoundException;
import org.example.onlineshop.cart.mapper.CartItemMapper;
import org.example.onlineshop.cart.model.Cart;
import org.example.onlineshop.cart.model.CartItem;
import org.example.onlineshop.product.model.Product;
import org.example.onlineshop.cart.repository.CartItemRepository;
import org.example.onlineshop.cart.repository.CartRepository;
import org.example.onlineshop.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartItem findById(Long id) {
        log.debug("Finding CartItem by id: {}", id);
        return cartItemRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("CartItem not found with id: {}", id);
                    return new ResourceNotFoundException("cartItem with id: " + id + " not found");
                });
    }

    @Transactional
    public CartItemResponseDTO addCartItem(Long cartId, CartItemRequestDTO request) {
        log.info("Adding item to cart. CartId: {}, ProductId: {}, Quantity: {}", cartId, request.getProductId(), request.getQuantity());

        if (request.getQuantity() <= 0) {
            log.warn("Invalid quantity provided: {}", request.getQuantity());
            throw new InvalidRequestException("Quantity must be greater than zero");
        }

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart with id " + cartId + " not found"));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + request.getProductId() + " not found"));

        Optional<CartItem> existingCartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(request.getProductId()))
                .findFirst();

        CartItem cartItem;

        if (existingCartItem.isPresent()) {
            cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
            log.debug("Increased quantity of existing cart item. New quantity: {}", cartItem.getQuantity());
        } else {
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(request.getQuantity());
            log.debug("Created new CartItem for product: {}", request.getProductId());
        }

        cartItemRepository.save(cartItem);
        log.info("CartItem saved successfully. CartItemId: {}", cartItem.getId());
        return CartItemMapper.toDTO(cartItem);
    }

    @Transactional
    public CartItemResponseDTO updateCartItemQuantity(Long cartItemId, CartItemRequestDTO request) {
        log.info("Updating quantity for CartItemId: {} with new quantity: {}", cartItemId, request.getQuantity());

        if (request.getQuantity() <= 0) {
            log.warn("Invalid quantity provided: {}", request.getQuantity());
            throw new InvalidRequestException("Quantity must be greater than zero");
        }

        CartItem cartItem = findById(cartItemId);
        cartItem.setQuantity(request.getQuantity());
        cartItemRepository.save(cartItem);

        log.info("CartItem quantity updated. CartItemId: {}", cartItemId);
        return CartItemMapper.toDTO(cartItem);
    }

    @Transactional
    public void removeCartItem(Long cartItemId) {
        log.info("Removing CartItem with id: {}", cartItemId);
        CartItem cartItem = findById(cartItemId);
        cartItemRepository.delete(cartItem);
        log.info("CartItem removed successfully. CartItemId: {}", cartItemId);
    }

    public Set<CartItemResponseDTO> getCartItemsByCartId(Long cartId) {
        log.info("Fetching CartItems for CartId: {}", cartId);
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart with id: " + cartId + " not found"));

        Set<CartItemResponseDTO> dtos = cart.getCartItems().stream()
                .map(CartItemMapper::toDTO)
                .collect(Collectors.toSet());

        log.info("Found {} items in cart with id: {}", dtos.size(), cartId);
        return dtos;
    }

    @Transactional
    public BigDecimal calculateItemTotal(Long cartItemId) {
        log.debug("Calculating total for CartItemId: {}", cartItemId);
        CartItem cartItem = findById(cartItemId);
        BigDecimal total = cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
        log.info("Total for CartItemId {}: {}", cartItemId, total);
        return total;
    }
}
