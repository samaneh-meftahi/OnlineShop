package org.example.onlineshop.product.dto.requestDto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class CouponRequestDTO {
    private String code;
    private String discountType;
    private BigDecimal discountValue;
    private int usageLimit;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;
}
