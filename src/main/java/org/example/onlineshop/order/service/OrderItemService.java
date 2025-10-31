package org.example.onlineshop.order.service;

import lombok.extern.slf4j.Slf4j;
import org.example.onlineshop.payment.dto.OrderItemRequestDTO;
import org.example.onlineshop.payment.dto.OrderItemResponseDTO;
import org.example.onlineshop.order.mapper.OrderItemMapper;
import org.example.onlineshop.order.model.Order;
import org.example.onlineshop.order.model.OrderItem;
import org.example.onlineshop.product.model.Product;
import org.example.onlineshop.order.repository.OrderItemRepository;
import org.example.onlineshop.order.repository.OrderRepository;
import org.example.onlineshop.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderItemService(OrderItemRepository orderItemRepository,
                            OrderRepository orderRepository,
                            ProductRepository productRepository) {
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public OrderItem findById(Long id) {
        log.debug("Finding OrderItem by id: {}", id);
        return orderItemRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("OrderItem with id {} not found", id);
                    return new NoSuchElementException("OrderItem with id " + id + " not found");
                });
    }

    public Set<OrderItemResponseDTO> listOrderItems(Long orderId) {
        log.info("Listing items for orderId={}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.warn("Order with id {} not found", orderId);
                    return new NoSuchElementException("Order with id " + orderId + " not found");
                });
        return order.getOrderItems().stream()
                .map(OrderItemMapper::toDto)
                .collect(Collectors.toSet());
    }

    public OrderItemResponseDTO addOrderItem(Long orderId, OrderItemRequestDTO dto) {
        log.info("Adding item to orderId={}, productId={}, quantity={}", orderId, dto.getProductId(), dto.getQuantity());
        validateOrderItem(dto);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.warn("Order not found: {}", orderId);
                    return new NoSuchElementException("Order not found");
                });

        Optional<OrderItem> existingItem = order.getOrderItems().stream()
                .filter(i -> i.getProduct().getId().equals(dto.getProductId()))
                .findFirst();

        if (existingItem.isPresent()) {
            OrderItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + dto.getQuantity());
            item.setPrice(dto.getPrice());
            OrderItem updated = orderItemRepository.save(item);
            log.info("Updated existing OrderItem id={} with new quantity={}", updated.getId(), updated.getQuantity());
            return OrderItemMapper.toDto(updated);
        }

        OrderItem orderItem = OrderItemMapper.toEntity(dto);
        orderItem.setOrder(order);
        OrderItem saved = orderItemRepository.save(orderItem);
        log.info("Created new OrderItem id={}", saved.getId());
        return OrderItemMapper.toDto(saved);
    }

    public OrderItemResponseDTO updateOrderItem(Long id, OrderItemRequestDTO updatedDto) {
        log.info("Updating OrderItem id={}", id);
        validateOrderItem(updatedDto);

        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("OrderItem not found: id={}", id);
                    return new NoSuchElementException("OrderItem with id " + id + " not found");
                });

        Order order = orderRepository.findById(updatedDto.getOrderId())
                .orElseThrow(() -> {
                    log.warn("Order not found: id={}", updatedDto.getOrderId());
                    return new NoSuchElementException("Order not found");
                });

        Product product = productRepository.findById(updatedDto.getProductId())
                .orElseThrow(() -> {
                    log.warn("Product not found: id={}", updatedDto.getProductId());
                    return new NoSuchElementException("Product not found");
                });

        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setPrice(updatedDto.getPrice());
        orderItem.setQuantity(updatedDto.getQuantity());

        OrderItem updated = orderItemRepository.save(orderItem);
        log.info("OrderItem id={} updated successfully", updated.getId());
        return OrderItemMapper.toDto(updated);
    }

    public void removeOrderItem(Long orderId, Long orderItemId) {
        log.info("Removing OrderItem id={} from orderId={}", orderItemId, orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.warn("Order with id {} not found", orderId);
                    return new NoSuchElementException("Order with id " + orderId + " not found");
                });

        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> {
                    log.warn("OrderItem with id {} not found", orderItemId);
                    return new NoSuchElementException("OrderItem with id " + orderItemId + " not found");
                });

        if (!order.getOrderItems().contains(orderItem)) {
            log.error("OrderItem id={} does not belong to Order id={}", orderItemId, orderId);
            throw new IllegalArgumentException("OrderItem does not belong to Order");
        }

        order.getOrderItems().remove(orderItem);
        orderRepository.save(order);
        orderItemRepository.delete(orderItem);
        log.info("OrderItem id={} removed successfully", orderItemId);
    }

    public void validateOrderItem(OrderItemRequestDTO orderItem) {
        log.debug("Validating OrderItemRequestDTO: {}", orderItem);
        if (orderItem == null) {
            throw new IllegalArgumentException("OrderItem must not be null");
        }
        if (orderItem.getProductId() == null) {
            throw new IllegalArgumentException("Product ID must not be null");
        }
        if (orderItem.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        if (orderItem.getPrice() == null || orderItem.getPrice().doubleValue() < 0) {
            throw new IllegalArgumentException("Price must be a positive number");
        }
        if (orderItem.getOrderId() == null) {
            throw new IllegalArgumentException("Order ID must not be null");
        }
    }
}
