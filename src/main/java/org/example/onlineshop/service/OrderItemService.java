package org.example.onlineshop.service;

import org.example.onlineshop.model.Order;
import org.example.onlineshop.model.OrderItem;
import org.example.onlineshop.repository.OrderItemRepository;
import org.example.onlineshop.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;

    public OrderItemService(OrderItemRepository orderItemRepository, OrderRepository orderRepository) {
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
    }

    public OrderItem findById(Long id) {
        return orderItemRepository.findById(id).orElseThrow(() -> new NoSuchElementException(""));
    }

    public List<OrderItem> listOrderItems(Long orderId) {
        Optional<Order> exitingOrder = orderRepository.findById(orderId);
        if (exitingOrder.isPresent()) {
            Order order = exitingOrder.get();
            return order.getOrderItems();
//            return orderItemRepository.findAll().stream().filter(orderItem -> orderItem.getOrder().getId().equals(orderId)).collect(Collectors.toList());
        } else {
            throw new NoSuchElementException("");
        }
    }

    public OrderItem addOrderItem(Long orderId, OrderItem orderItem) {
        Optional<Order> exitingOrder = orderRepository.findById(orderId);
        if (exitingOrder.isPresent()) {
            Order order = exitingOrder.get();
            order.getOrderItems().add(orderItem);
            orderRepository.save(order);
            return orderItemRepository.save(orderItem);
        } else {
            throw new NoSuchElementException("");
        }
    }

    public OrderItem updateOrderItem(Long id, OrderItem updateOrderItem) {
        Optional<OrderItem> exitingOrderItem = orderItemRepository.findById(id);
        if (exitingOrderItem.isPresent()) {
            OrderItem orderItem = exitingOrderItem.get();
            orderItem.setOrder(updateOrderItem.getOrder());
            orderItem.setPrice(updateOrderItem.getPrice());
            orderItem.setProduct(updateOrderItem.getProduct());
            orderItem.setQuantity(updateOrderItem.getQuantity());
            return orderItemRepository.save(orderItem);
        } else {
            throw new NoSuchElementException("");
        }
    }

    public void removeOrderItem(Long orderId, Long orderItemId) {
        Optional<Order> exitingOrder = orderRepository.findById(orderId);
        Optional<OrderItem> exitingOrderItem = orderItemRepository.findById(orderItemId);
        if (exitingOrderItem.isPresent() && exitingOrder.isPresent()) {
            Order order = exitingOrder.get();
            OrderItem orderItem = exitingOrderItem.get();
            order.getOrderItems().remove(orderItem);
            orderRepository.save(order);
        } else {
            throw new NoSuchElementException("");
        }
    }

    public void validateOrderItem(OrderItem orderItem) {
    }
}
