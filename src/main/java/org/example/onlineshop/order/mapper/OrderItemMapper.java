package org.example.onlineshop.order.mapper;

import org.example.onlineshop.payment.dto.OrderItemRequestDTO;
import org.example.onlineshop.payment.dto.OrderItemResponseDTO;
import org.example.onlineshop.order.model.Order;
import org.example.onlineshop.order.model.OrderItem;
import org.example.onlineshop.product.model.Product;

public class OrderItemMapper {
    public static OrderItemResponseDTO toDto(OrderItem orderItem){
        OrderItemResponseDTO dto=new OrderItemResponseDTO();
        dto.setId(orderItem.getId());
        dto.setProductName(orderItem.getProduct().getTitle());
        dto.setQuantity(orderItem.getQuantity());
        dto.setPrice(orderItem.getPrice());
        dto.setPrice(orderItem.getPrice());
        dto.setProductId(orderItem.getProduct().getId());

        return dto;
    }

    public static OrderItem toEntity(OrderItemRequestDTO dto){
        OrderItem orderItem = new OrderItem();
        orderItem.setId(dto.getId());
        orderItem.setQuantity(dto.getQuantity());
        orderItem.setPrice(dto.getPrice());

        // Set Product by ID
        Product product = new Product();
        product.setId(dto.getProductId());
        orderItem.setProduct(product);

        // Set Order by ID
        Order order = new Order();
        order.setId(dto.getOrderId());
        orderItem.setOrder(order);

        return orderItem;
    }
}
