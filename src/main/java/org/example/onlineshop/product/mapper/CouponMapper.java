package org.example.onlineshop.product.mapper;

import org.example.onlineshop.product.dto.requestDto.CouponRequestDTO;
import org.example.onlineshop.product.dto.responseDto.CouponResponseDTO;
import org.example.onlineshop.product.model.Coupon;

public class CouponMapper {
    public static CouponResponseDTO toDto(Coupon coupon){
        CouponResponseDTO dto=new CouponResponseDTO();

        dto.setId(coupon.getId());
        dto.setCode(coupon.getCode());
        dto.setDiscountType(coupon.getDiscountType());
        dto.setUsedCount(coupon.getUsedCount());
        dto.setUsageLimit(coupon.getUsageLimit());
        dto.setValidFrom(coupon.getValidFrom());
        dto.setUsedCount(coupon.getUsedCount());
        dto.setValidTo(coupon.getValidTo());

        return dto;
    }

    public static Coupon toEntity(CouponRequestDTO dto){
        Coupon coupon=new Coupon();

        coupon.setDiscountValue(dto.getDiscountValue());
        coupon.setValidTo(dto.getValidTo());
        coupon.setDiscountType(dto.getDiscountType());
        coupon.setUsageLimit(dto.getUsageLimit());
        coupon.setValidFrom(coupon.getValidFrom());

        return coupon;
    }
}
