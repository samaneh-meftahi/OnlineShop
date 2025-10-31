package org.example.onlineshop.product.service;

import jakarta.transaction.Transactional;
import org.example.onlineshop.product.dto.requestDto.CouponRequestDTO;
import org.example.onlineshop.product.dto.responseDto.CouponResponseDTO;
import org.example.onlineshop.common.exception.InvalidRequestException;
import org.example.onlineshop.common.exception.ResourceNotFoundException;
import org.example.onlineshop.product.mapper.CouponMapper;
import org.example.onlineshop.product.model.Coupon;
import org.example.onlineshop.cart.repository.CartRepository;
import org.example.onlineshop.product.repository.CouponRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class CouponService {

    private static final Logger logger = LoggerFactory.getLogger(CouponService.class);

    private final CouponRepository couponRepository;
    private final CartRepository cartRepository;

    public CouponService(CouponRepository couponRepository, CartRepository cartRepository) {
        this.couponRepository = couponRepository;
        this.cartRepository = cartRepository;
    }

    @Transactional
    public CouponResponseDTO createCoupon(CouponRequestDTO couponRequestDTO) {
        logger.info("Creating coupon with code: {}", couponRequestDTO.getCode());

        Coupon coupon = CouponMapper.toEntity(couponRequestDTO);
        validateCoupon(coupon);

        Coupon savedCoupon = couponRepository.save(coupon);
        logger.info("Coupon created with id: {}", savedCoupon.getId());

        return CouponMapper.toDto(savedCoupon);
    }

    public Coupon findById(Long couponId) {
        return couponRepository.findById(couponId)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon with id " + couponId + " not found"));
    }

    @Transactional
    public CouponResponseDTO updateCoupon(Long couponId, CouponRequestDTO requestDTO) {
        logger.info("Updating coupon with id: {}", couponId);

        Coupon existing = findById(couponId);
        existing.setCode(requestDTO.getCode());
        existing.setDiscountValue(requestDTO.getDiscountValue());
        existing.setDiscountType(requestDTO.getDiscountType());
        existing.setUsageLimit(requestDTO.getUsageLimit());
        existing.setValidTo(requestDTO.getValidTo());

        validateCoupon(existing);

        Coupon updated = couponRepository.save(existing);
        logger.info("Coupon updated with id: {}", updated.getId());

        return CouponMapper.toDto(updated);
    }

    @Transactional
    public void removeCoupon(Long couponId) {
        logger.warn("Removing coupon with id: {}", couponId);
        Coupon coupon = findById(couponId);
        couponRepository.delete(coupon);
    }

    public void validateCoupon(Coupon coupon) {
        if (coupon == null) {
            throw new InvalidRequestException("Coupon must not be null");
        }
        if (coupon.getCode() == null || coupon.getCode().isBlank()) {
            throw new InvalidRequestException("Coupon code must not be empty");
        }
        if (coupon.getDiscount_value() == null || coupon.getDiscount_value().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidRequestException("Discount value must be greater than zero");
        }
        if (coupon.getDiscountType() == null) {
            throw new InvalidRequestException("Discount type must be specified");
        }
        if (coupon.getValidTo() != null && coupon.getValidTo().isBefore(LocalDateTime.now())) {
            throw new InvalidRequestException("Coupon expiration date must be in the future");
        }
    }
}
