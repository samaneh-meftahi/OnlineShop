package org.example.onlineshop.product.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "coupons")
public class Coupon {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Getter
    private String code;
    @Getter
    @Column(name = "discount_type")
    private String discountType;
    @Column(name = "discount_value")
    private BigDecimal discountValue;
    @Getter
    @Column(name = "usage_limit")
    private int usageLimit;
    @Getter
    @Column(name = "used_count")
    private int usedCount;
    @Getter
    @Column(name = "valid_from")
    private LocalDateTime validFrom;
    @Getter
    @Column(name = "valid_to")
    private LocalDateTime validTo;


    public void setId(Long id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscount_value() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discount_value) {
        this.discountValue = discount_value;
    }

    public void setUsageLimit(int usageLimit) {
        this.usageLimit = usageLimit;
    }

    public void setUsedCount(int usedCount) {
        this.usedCount = usedCount;
    }

    public void setValidFrom(LocalDateTime validFrom) {
        this.validFrom = validFrom;
    }

    public void setValidTo(LocalDateTime validTo) {
        this.validTo = validTo;
    }
}
