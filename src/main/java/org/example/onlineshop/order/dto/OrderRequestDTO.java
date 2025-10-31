package org.example.onlineshop.order.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.onlineshop.payment.dto.OrderItemRequestDTO;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
@Getter
@Setter
public class OrderRequestDTO {
    private Long id;
    private String status;
    private Long userId;
    private String address;
    private String shippingMethod;
    private String trackingCode;
    private BigDecimal totalPrice;
    private Set<OrderItemRequestDTO> orderItems=new HashSet<>();
}
