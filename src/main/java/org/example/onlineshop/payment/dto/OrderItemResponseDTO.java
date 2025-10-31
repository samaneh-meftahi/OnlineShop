package org.example.onlineshop.payment.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class OrderItemResponseDTO {
    private Long id;
    private String productName;
    private Long productId;
    private Long orderId;
    private int quantity;
    private BigDecimal price;
}
