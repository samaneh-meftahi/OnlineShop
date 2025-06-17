package org.example.onlineshop.service;

import org.example.onlineshop.model.Cart;
import org.example.onlineshop.model.Order;
import org.example.onlineshop.repository.CartRepository;
import org.example.onlineshop.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;

    public OrderService(OrderRepository orderRepository, CartRepository cartRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
    }

    public Order createOrderFromCart(Long cartId) {
        Optional<Cart> exitingCart = cartRepository.findById(cartId);
        if (exitingCart.isPresent() && !exitingCart.get().getCartItem().isEmpty()) {
            Cart cart = exitingCart.get();
            Order order = new Order();
            order.setOrderItems(cart.getCartItem());
            order.setUser(cart.getUser());
            return orderRepository.save(order);
        } else {
            throw new NoSuchElementException("");
        }
    }

    public Optional<Order> findById(Long id){
        return orderRepository.findById(id);
    }

    public Order updateOrder(Long orderId,Order updateOrder){
        Optional<Order> exitingOrder=orderRepository.findById(orderId);
        if (exitingOrder.isPresent()){
            validateOrder(updateOrder);
            Order order=exitingOrder.get();
            order.setOrderItems(updateOrder.getOrderItems());
            order.setShippingMethod(updateOrder.getShippingMethod());
            order.setAddress(updateOrder.getAddress());
            order.setStatus(updateOrder.getStatus());
            order.setTrackingCode(updateOrder.getTrackingCode());
            order.setUser(updateOrder.getUser());
            return orderRepository.save(order);
        }else {
            throw new NoSuchElementException("");
        }
    }

    public void cancelOrder(Long orderId){
        Optional<Order> exitingOrder=findById(orderId);
        if (exitingOrder.isPresent()){
            Order order=exitingOrder.get();
            if (order.getStatus().equals("PENDING")){
                orderRepository.delete(order);
            }
        }
    }
    public void validateOrder(Order order){
        if(order.getOrderItems().isEmpty()){
            throw new IllegalArgumentException("");
        }
        if (order.getAddress().isEmpty()){
            throw new IllegalArgumentException("");
        }
        if ((order.getUser()==null)){
            throw new IllegalArgumentException("");

        }
        if(order.getShippingMethod().isEmpty()){
            throw new IllegalArgumentException("");
        }
        if (order.getStatus().isEmpty()){
            throw new IllegalArgumentException("");
        }
        if (order.getTotalPrice()==null){
            throw new IllegalArgumentException("");
        }
    }
}
