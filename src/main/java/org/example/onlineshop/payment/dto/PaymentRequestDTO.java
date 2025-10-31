package org.example.onlineshop.payment.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.onlineshop.payment.payment.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter
@Setter
public class PaymentRequestDTO {
    private Long orderId;
    private BigDecimal amount;
    private String method;
    private PaymentStatus status;
    private String transactionReference;
    private LocalDateTime paidAt;
    private LocalDateTime refundedAt;
    private String refundReason;
}
