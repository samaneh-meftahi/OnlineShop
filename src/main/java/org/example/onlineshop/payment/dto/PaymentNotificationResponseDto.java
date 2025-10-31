package org.example.onlineshop.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentNotificationResponseDto {
    private String paymentId; // شناسه پرداخت در سیستم ما یا درگاه
    private String transactionReference; // شناسه تراکنش در درگاه
    private String status; // وضعیت پرداخت (SUCCESS, FAILED, PENDING)
    private BigDecimal amount; // مبلغ تراکنش
    private LocalDateTime timestamp; // زمان تراکنش
    private String message; // پیام یا توضیح اضافی

    // Constructors
    public PaymentNotificationResponseDto() {}

    // Getters and Setters
    public String getPaymentId() {
        return paymentId;
    }
    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }
    public String getTransactionReference() {
        return transactionReference;
    }
    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
