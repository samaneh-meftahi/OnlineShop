package org.example.onlineshop.order.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.onlineshop.payment.dto.OrderItemResponseDTO;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
@Getter
@Setter
public class OrderResponseDTO {
    private Long id;
    private Long userId;
    private String address;
    private String shippingMethod;
    private String trackingCode;
    private Set<OrderItemResponseDTO> orderItems=new HashSet<>();
    private BigDecimal totalPrice;
    private String status;

}
