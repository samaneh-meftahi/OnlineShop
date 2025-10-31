package org.example.onlineshop.payment.mapper;

import org.example.onlineshop.payment.dto.PaymentNotificationRequestDto;
import org.example.onlineshop.payment.dto.PaymentNotificationResponseDto;

public class PaymentNotificationMapper {

    public static PaymentNotificationResponseDto toResponseDto(PaymentNotificationRequestDto requestDto) {
        PaymentNotificationResponseDto responseDto = new PaymentNotificationResponseDto();

        responseDto.setPaymentId(requestDto.getPaymentId());
        responseDto.setTransactionReference(requestDto.getTransactionReference());
        responseDto.setStatus(requestDto.getStatus());
        responseDto.setAmount(requestDto.getAmount());
        responseDto.setTimestamp(requestDto.getTimestamp());
        responseDto.setMessage(requestDto.getMessage());

        return responseDto;
    }

    public static PaymentNotificationRequestDto toRequestDto(PaymentNotificationResponseDto responseDto) {
        PaymentNotificationRequestDto requestDto = new PaymentNotificationRequestDto();

        requestDto.setPaymentId(responseDto.getPaymentId());
        requestDto.setTransactionReference(responseDto.getTransactionReference());
        requestDto.setStatus(responseDto.getStatus());
        requestDto.setAmount(responseDto.getAmount());
        requestDto.setTimestamp(responseDto.getTimestamp());
        requestDto.setMessage(responseDto.getMessage());

        return requestDto;
    }
}
