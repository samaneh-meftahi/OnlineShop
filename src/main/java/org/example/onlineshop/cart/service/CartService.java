package org.example.onlineshop.cart.service;

import jakarta.transaction.Transactional;
import org.example.onlineshop.cart.model.Cart;
import org.example.onlineshop.cart.model.CartItem;
import org.example.onlineshop.cart.dto.CartRequestDTO;
import org.example.onlineshop.cart.dto.CartResponseDTO;
import org.example.onlineshop.common.exception.InvalidRequestException;
import org.example.onlineshop.common.exception.ResourceNotFoundException;
import org.example.onlineshop.cart.mapper.CartMapper;
import org.example.onlineshop.product.model.Product;
import org.example.onlineshop.cart.repository.CartItemRepository;
import org.example.onlineshop.cart.repository.CartRepository;
import org.example.onlineshop.product.repository.ProductRepository;
import org.example.onlineshop.user.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartService.class);

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    @Autowired
    public CartService(CartRepository cartRepository, ProductRepository productRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Transactional
    public CartResponseDTO createCart(CartRequestDTO cartRequestDTO) {
        logger.info("Creating a new cart...");
        List<Product> products = productRepository.findAll();
        User user = new User();
        user.setId(cartRequestDTO.getUserId());

        Cart cart = CartMapper.toEntity(cartRequestDTO, user, products);
        validateCart(cart);
        Cart savedCart = cartRepository.save(cart);

        logger.info("Cart created with ID: {}", savedCart.getId());
        return CartMapper.toDTO(savedCart);
    }

    public Optional<Cart> findById(Long id) {
        return cartRepository.findById(id);
    }

    public Optional<Cart> findByUserId(Long userId) {
        return cartRepository.findByUser_Id(userId);
    }

    public void removeCart(Long id) {
        logger.info("Removing cart with ID: {}", id);
        Cart cart = findById(id).orElseThrow(() -> new ResourceNotFoundException("Cart with id " + id + " not found"));
        cartRepository.delete(cart);
        logger.info("Cart removed successfully.");
    }

    @Transactional
    public Cart addProductToCart(Long cartId, Long productId, int quantity) {
        logger.info("Adding product ID {} to cart ID {} with quantity {}", productId, cartId, quantity);
        Cart cart = findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart with id " + cartId + " not found"));
        Product product = productRepository.findById(productId).orElseThrow(() ->
                new ResourceNotFoundException("Product with id " + productId + " not found"));

        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseGet(() -> {
                    CartItem newItem = new CartItem();
                    newItem.setProduct(product);
                    newItem.setCart(cart);
                    cart.getCartItems().add(newItem);
                    return newItem;
                });

        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        cartItemRepository.save(cartItem);
        logger.info("Product added or updated in cart.");

        return cartRepository.save(cart);
    }

    @Transactional
    public Cart updateProductQuantity(Long cartId, Long productId, int quantity) {
        logger.info("Updating product quantity for product ID {} in cart ID {}", productId, cartId);
        Cart cart = findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart with id " + cartId + " not found"));

        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + productId + " not found in cart"));

        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        if (cartItem.getQuantity() <= 0) {
            cart.getCartItems().remove(cartItem);
            cartItemRepository.delete(cartItem);
            logger.info("Product removed from cart due to non-positive quantity.");
        } else {
            cartItemRepository.save(cartItem);
            logger.info("Product quantity updated.");
        }

        return cartRepository.save(cart);
    }

    @Transactional
    public Cart removeProductFromCart(Long cartId, Long productId) {
        logger.info("Removing product ID {} from cart ID {}", productId, cartId);
        Cart cart = findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart with id " + cartId + " not found"));

        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + productId + " not found in cart"));

        cart.getCartItems().remove(cartItem);
        cartItemRepository.delete(cartItem);
        logger.info("Product removed from cart.");

        return cartRepository.save(cart);
    }

    @Transactional
    public void clearCart(Long cartId) {
        logger.info("Clearing cart ID {}", cartId);
        Cart cart = findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart with id " + cartId + " not found"));
        cartItemRepository.deleteAll(cart.getCartItems());
        cart.getCartItems().clear();
        cartRepository.save(cart);
        logger.info("Cart cleared successfully.");
    }

    @Transactional
    public BigDecimal calculateCartTotal(Long cartId) {
        logger.info("Calculating total for cart ID {}", cartId);
        Cart cart = findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart with id " + cartId + " not found"));
        return cart.getCartItems().stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void applyCouponToCart() {
        logger.warn("applyCouponToCart not implemented yet.");
        // To be implemented
    }

    public void validateCart(Cart cart) {
        if (cart.getUser() == null) {
            throw new InvalidRequestException("User must not be null");
        }

        if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            throw new InvalidRequestException("Cart must have at least one item");
        }

        for (CartItem item : cart.getCartItems()) {
            if (item.getProduct() == null) {
                throw new InvalidRequestException("Product in cart item cannot be null");
            }

            if (item.getQuantity() <= 0) {
                throw new InvalidRequestException("Quantity must be greater than zero for product: " + item.getProduct().getId());
            }

            if (item.getQuantity() > item.getProduct().getStock()) {
                throw new InvalidRequestException("Not enough stock for product: " + item.getProduct().getId());
            }
        }
    }
}
