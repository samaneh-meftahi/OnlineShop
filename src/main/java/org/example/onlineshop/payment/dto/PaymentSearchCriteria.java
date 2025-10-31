package org.example.onlineshop.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentSearchCriteria {
    private Long orderId;
    private String status; // مثلا "PAID", "PENDING", "FAILED"
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private Long userId; // اگر بخواهی بر اساس کاربر فیلتر کنی

    // Constructors
    public PaymentSearchCriteria() {}

    // Getters and Setters
    public Long getOrderId() {
        return orderId;
    }
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public LocalDateTime getFromDate() {
        return fromDate;
    }
    public void setFromDate(LocalDateTime fromDate) {
        this.fromDate = fromDate;
    }
    public LocalDateTime getToDate() {
        return toDate;
    }
    public void setToDate(LocalDateTime toDate) {
        this.toDate = toDate;
    }
    public BigDecimal getMinAmount() {
        return minAmount;
    }
    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }
    public BigDecimal getMaxAmount() {
        return maxAmount;
    }
    public void setMaxAmount(BigDecimal maxAmount) {
        this.maxAmount = maxAmount;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
