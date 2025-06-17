package org.example.onlineshop.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "coupons")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    @Column(name = "discount_type")
    private String discountType;
    private BigDecimal discount_value;
    @Column(name = "usage_limit")
    private int usageLimit;
    @Column(name = "used_count")
    private int usedCount;
    @Column(name = "valid_from")
    private LocalDateTime validFrom;
    @Column(name = "valid_to")
    private LocalDateTime valid_to;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscount_value() {
        return discount_value;
    }

    public void setDiscount_value(BigDecimal discount_value) {
        this.discount_value = discount_value;
    }

    public int getUsageLimit() {
        return usageLimit;
    }

    public void setUsageLimit(int usageLimit) {
        this.usageLimit = usageLimit;
    }

    public int getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(int usedCount) {
        this.usedCount = usedCount;
    }

    public LocalDateTime getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDateTime validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDateTime getValid_to() {
        return valid_to;
    }

    public void setValid_to(LocalDateTime valid_to) {
        this.valid_to = valid_to;
    }
}
