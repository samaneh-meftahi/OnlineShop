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

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    @Autowired
    public CartService(CartRepository cartRepository, ProductRepository productRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public Cart createCart(Cart cart) {
        validateCart(cart);
        return cartRepository.save(cart);
    }

    public void removeCart(Long id) {
        Cart cart = findById(id).orElseThrow(() -> new NoSuchElementException(""));
        cartRepository.delete(cart);
    }

    public Optional<Cart> findById(Long id) {
        return cartRepository.findById(id);
    }

    public Optional<Cart> findByUser_id(Long id) {
        return cartRepository.findByUser_Id(id);
    }

    @Transactional
    public BigDecimal calculateCartTotal(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new NoSuchElementException("Cart with id " + cartId + " not found"));
        return cart.getCartItem().stream().map(item ->
                        item.getProduct().getPrice().
                                multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional
    public Cart addProductToCart(Long cartId, Long productId, int quantity) {
        Cart cart = findById(cartId).orElseThrow(() -> new NoSuchElementException("Cart with id " + cartId + " not found"));
        Product product = productRepository.findById(productId).orElseThrow(() ->
                new NoSuchElementException("Cart with id " + cartId + " not found"));
        Optional<CartItem> existingCartItem = cart.getCartItem().stream().filter(item -> item.getProduct().getId().equals(productId)).findFirst();
        if (existingCartItem.isPresent()) {
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setCart(cart);
            cartItem.setQuantity(quantity);
            cart.getCartItem().add(cartItem);
            cartItemRepository.save(cartItem);
        }
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart removeProductFromCart(Long cartId, Long productId) {
        Cart cart = findById(cartId).orElseThrow(() -> new NoSuchElementException("Cart with id " + cartId + " not found"));
        Optional<CartItem> cartItemOptional = cart.getCartItem().stream().filter(item -> item.getProduct().getId().equals(productId)).findFirst();
        if (cartItemOptional.isPresent()) {
            CartItem cartItem = cartItemOptional.get();
            cart.getCartItem().remove(cartItem);
            cartItemRepository.delete(cartItem);
            return cartRepository.save(cart);
        } else {
            throw new NoSuchElementException("product with id " + cartId + " not found in cart");
        }
    }

    @Transactional
    public Cart updateProductQuantity(Long cartId, Long productId, int quantity) {
        Cart cart = findById(cartId).orElseThrow(() -> new NoSuchElementException("Cart with id " + cartId + " not found in cart"));
        Optional<CartItem> exitingCartItem = cart.getCartItem().stream().filter(item -> item.getProduct().getId().equals(productId)).findFirst();
        if (exitingCartItem.isPresent()) {
            CartItem cartItem = exitingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            if (cartItem.getQuantity() <= 0) {
                cart.getCartItem().remove(cartItem);
                cartItemRepository.delete(cartItem);
            } else {
                cartItemRepository.save(cartItem);
            }
            return cartRepository.save(cart);
        } else {
            throw new NoSuchElementException("Product with id " + productId + " not found in cart");
        }
    }

    public void applyCouponToCart() {

    }

    void clearCart(Long cartId) {
        Cart cart = findById(cartId).orElseThrow(() -> new NoSuchElementException(""));
        cartItemRepository.deleteAll(cart.getCartItem());
        cart.getCartItem().clear();
        cartRepository.save(cart);
    }

    public void validateCart(Cart cart) {
        if (cart.getUser() == null) {
            throw new IllegalArgumentException("User must not be null");
        }
        if (cart.getCartItem() == null || cart.getCartItem().isEmpty()) {
            throw new IllegalArgumentException("Cart must have at least one item");
        }
        for (CartItem item : cart.getCartItem()) {
            if (item.getQuantity() <= 0) {
                throw new IllegalArgumentException("Quantity for product " + item.getProduct().getId() + " must be greater than zero");
            }
            if (item.getProduct() == null) {
                throw new IllegalArgumentException("Product in cart item cannot be null");
            }
            if (item.getQuantity() > item.getProduct().getStock()) {
                throw new IllegalArgumentException("Not enough stock for product " + item.getProduct().getId());
            }
        }
    }
}