package org.example.onlineshop.service;

import jakarta.transaction.Transactional;
import org.example.onlineshop.model.Cart;
import org.example.onlineshop.model.CartItem;
import org.example.onlineshop.model.Product;
import org.example.onlineshop.repository.CartItemRepository;
import org.example.onlineshop.repository.CartRepository;
import org.example.onlineshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

@Service
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CartItemService(CartItemRepository cartItemRepository, CartRepository cartRepository, ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    public CartItem findById(Long id) {
        return cartItemRepository.findById(id).orElseThrow(() -> new NoSuchElementException("cartItem with id: "+id+ "not found"));
    }

    @Transactional
    public CartItem addCartItem(Long cartId, Long productId, int quantity) {
        if (quantity<=0){
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new NoSuchElementException("Cart with id " + cartId + " not found"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new NoSuchElementException("product with id " + productId + " not found"));
        Optional<CartItem> existingCartItem = cart.getCartItem().stream().filter(item -> item.getProduct().getId().equals(productId)).findFirst();
        CartItem cartItem;
        if (existingCartItem.isPresent()) {
            cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
        }
        return cartItemRepository.save(cartItem);
    }

    @Transactional
    public CartItem updateCartItemQuantity(Long cartItemId, int quantity) {
        CartItem cartItem = findById(cartItemId);
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        } else {
            cartItem.setQuantity(quantity);
        }
        return cartItemRepository.save(cartItem);
    }

    @Transactional
    public void removeCartItem(Long cartItemId) {
        CartItem cartItem = findById(cartItemId);
        cartItemRepository.delete(cartItem);
    }

    public Set<CartItem> getCartItemsByCartId(Long cartId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new NoSuchElementException("cart with id: "+cartId+"not found"));
        return cart.getCartItem();
    }

    @Transactional
    public BigDecimal calculateItemTotal(Long cartItemId) {
        CartItem cartItem = findById(cartItemId);
        BigDecimal price = cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
        return price;
    }
}
