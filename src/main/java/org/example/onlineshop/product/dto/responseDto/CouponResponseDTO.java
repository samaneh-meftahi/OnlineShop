package org.example.onlineshop.product.dto.responseDto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter
@Setter
public class CouponResponseDTO {
    private Long id;
    private String code;
    private String discountType;
    private BigDecimal discountValue;
    private int usageLimit;
    private int usedCount;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;
}
