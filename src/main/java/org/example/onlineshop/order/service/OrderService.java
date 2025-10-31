package org.example.onlineshop.order.service;

import jakarta.transaction.Transactional;
import org.example.onlineshop.cart.model.Cart;
import org.example.onlineshop.cart.model.CartItem;
import org.example.onlineshop.order.model.Order;
import org.example.onlineshop.order.model.OrderItem;
import org.example.onlineshop.order.repository.OrderRepository;
import org.example.onlineshop.order.dto.OrderRequestDTO;
import org.example.onlineshop.order.dto.OrderResponseDTO;
import org.example.onlineshop.common.exception.ResourceNotFoundException;
import org.example.onlineshop.order.mapper.OrderItemMapper;
import org.example.onlineshop.order.mapper.OrderMapper;
import org.example.onlineshop.cart.repository.CartRepository;
import org.example.onlineshop.user.repository.UserRepository;
import org.example.onlineshop.user.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository,
                        CartRepository cartRepository,
                        UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public OrderResponseDTO createOrderFromCart(Long cartId) {
        logger.info("Creating order from cartId: {}", cartId);

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new NoSuchElementException("Cart with id " + cartId + " not found"));

        Set<CartItem> cartItems = cart.getCartItems();
        if (cartItems == null || cartItems.isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(cart.getUser());
        order.setStatus("PENDING");
        order.setCreatedAt(LocalDateTime.now());

        Set<OrderItem> orderItems = cartItems.stream()
                .map(this::mapCartItemToOrderItem)
                .peek(item -> item.setOrder(order))
                .collect(Collectors.toSet());

        order.setOrderItems(orderItems);
        order.setTotalPrice(calculateTotalPrice(orderItems));

        Order savedOrder = orderRepository.save(order);

        cartItems.clear();
        cartRepository.save(cart);

        logger.info("Order created successfully with id: {}", savedOrder.getId());

        return OrderMapper.toDto(savedOrder);
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    public OrderResponseDTO updateOrder(Long orderId, OrderRequestDTO updatedOrder) {
        logger.info("Updating order with id: {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order with id " + orderId + " not found"));

        User user = userRepository.findById(updatedOrder.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + updatedOrder.getUserId() + " not found"));

        validateOrder(updatedOrder);

        order.setOrderItems(updatedOrder.getOrderItems().stream()
                .map(OrderItemMapper::toEntity)
                .peek(item -> item.setOrder(order))
                .collect(Collectors.toSet()));

        order.setShippingMethod(updatedOrder.getShippingMethod());
        order.setAddress(updatedOrder.getAddress());
        order.setStatus(updatedOrder.getStatus());
        order.setTrackingCode(updatedOrder.getTrackingCode());
        order.setUser(user);
        order.setTotalPrice(calculateTotalPrice(order.getOrderItems()));

        orderRepository.save(order);

        logger.info("Order with id {} updated successfully.", orderId);

        return OrderMapper.toDto(order);
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        logger.info("Attempting to cancel order with id: {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order with id " + orderId + " not found"));

        if ("PENDING".equalsIgnoreCase(order.getStatus())) {
            orderRepository.delete(order);
            logger.info("Order with id {} cancelled successfully.", orderId);
        } else {
            logger.warn("Order with id {} could not be cancelled. Current status: {}", orderId, order.getStatus());
            throw new IllegalStateException("Only PENDING orders can be cancelled. Current status: " + order.getStatus());
        }
    }

    public void validateOrder(OrderRequestDTO order) {
        if (order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
            throw new IllegalArgumentException("Order must have at least one order item");
        }
        if (order.getAddress() == null || order.getAddress().isBlank()) {
            throw new IllegalArgumentException("Order address must not be empty");
        }
        if (order.getUserId() == null) {
            throw new IllegalArgumentException("Order must have a valid user");
        }
        if (order.getShippingMethod() == null || order.getShippingMethod().isBlank()) {
            throw new IllegalArgumentException("Shipping method must not be empty");
        }
        if (order.getStatus() == null || order.getStatus().isBlank()) {
            throw new IllegalArgumentException("Order status must not be empty");
        }
        if (order.getTotalPrice() == null || order.getTotalPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Total price must be positive");
        }
    }

    private OrderItem mapCartItemToOrderItem(CartItem cartItem) {
        OrderItem item = new OrderItem();
        item.setProduct(cartItem.getProduct());
        item.setQuantity(cartItem.getQuantity());
        item.setPrice(cartItem.getProduct().getPrice());
        return item;
    }

    private BigDecimal calculateTotalPrice(Set<OrderItem> items) {
        return items.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
