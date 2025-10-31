package org.example.onlineshop.payment.mapper;

import org.example.onlineshop.payment.dto.PaymentRequestDTO;
import org.example.onlineshop.payment.dto.PaymentResponseDTO;
import org.example.onlineshop.order.model.Order;
import org.example.onlineshop.payment.model.Payment;

public class PaymentMapper {
    public static PaymentResponseDTO toDto(Payment payment){
        PaymentResponseDTO dto=new PaymentResponseDTO();
        dto.setId(payment.getId());
        dto.setStatus(payment.getStatus());
        dto.setAmount(payment.getAmount());
        dto.setMethod(payment.getMethod());
        dto.setPaidAt(payment.getPaidAt());
        dto.setOrderId(payment.getOrder().getId());
        dto.setTransactionReference(payment.getTransactionReference());
        dto.setRefundReason(payment.getRefundReason());
        dto.setRefundedAt(payment.getRefundedAt());

        return dto;
    }

    public static Payment toEntity(PaymentRequestDTO dto){
        Payment payment = new Payment();
        payment.setStatus(dto.getStatus());
        payment.setAmount(dto.getAmount());
        payment.setMethod(dto.getMethod());
        payment.setPaidAt(dto.getPaidAt());
        payment.setTransactionReference(dto.getTransactionReference());
        payment.setRefundReason(dto.getRefundReason());
        payment.setRefundedAt(dto.getRefundedAt());

        Order order = new Order();
        order.setId(dto.getOrderId());
        payment.setOrder(order);

        return payment;
    }
}
