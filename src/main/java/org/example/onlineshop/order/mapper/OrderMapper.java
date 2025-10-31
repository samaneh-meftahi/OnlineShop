package org.example.onlineshop.order.mapper;

import org.example.onlineshop.order.dto.OrderRequestDTO;
import org.example.onlineshop.payment.dto.OrderItemResponseDTO;
import org.example.onlineshop.order.dto.OrderResponseDTO;
import org.example.onlineshop.order.model.Order;
import org.example.onlineshop.order.model.OrderItem;
import org.example.onlineshop.product.model.Product;
import org.example.onlineshop.user.model.User;

import java.util.Set;
import java.util.stream.Collectors;

public class OrderMapper {
    public static OrderResponseDTO toDto(Order order) {
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
        orderResponseDTO.setId(order.getId());
        orderResponseDTO.setUserId(order.getUser().getId());
        orderResponseDTO.setAddress(order.getAddress());
        orderResponseDTO.setShippingMethod(order.getShippingMethod());
        orderResponseDTO.setTrackingCode(order.getTrackingCode());
        orderResponseDTO.setTotalPrice(order.getTotalPrice());
        orderResponseDTO.setStatus(order.getStatus());

        Set<OrderItemResponseDTO> itemDTOs = order.getOrderItems()
                .stream()
                .map(OrderItemMapper::toDto)
                .collect(Collectors.toSet());

        orderResponseDTO.setOrderItems(itemDTOs);


        return orderResponseDTO;
    }

    public static Order toEntity(OrderRequestDTO dto) {
        Order order = new Order();
        order.setId(dto.getId());
        order.setAddress(dto.getAddress());
        order.setShippingMethod(dto.getShippingMethod());
        order.setTrackingCode(dto.getTrackingCode());

        User user = new User();
        user.setId(dto.getUserId());
        order.setUser(user);

        Set<OrderItem> items = dto.getOrderItems()
                .stream()
                .map(itemDto -> {
                    OrderItem item = new OrderItem();
                    item.setId(itemDto.getId());

                    Product product = new Product();
                    product.setId(itemDto.getProductId());
                    item.setProduct(product);

                    item.setQuantity(itemDto.getQuantity());
                    item.setPrice(itemDto.getPrice());
                    item.setOrder(order);

                    return item;
                })
                .collect(Collectors.toSet());

        order.setOrderItems(items);

        return order;
    }
}
