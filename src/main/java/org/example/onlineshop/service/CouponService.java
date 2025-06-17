package org.example.onlineshop.service;

import com.nimbusds.jose.crypto.impl.XC20P;
import jakarta.transaction.Transactional;
import org.example.onlineshop.model.Cart;
import org.example.onlineshop.model.Coupon;
import org.example.onlineshop.repository.CartRepository;
import org.example.onlineshop.repository.CouponsRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CouponService {
    private final CouponsRepository couponsRepository;
    private final CartRepository cartRepository;

    public CouponService(CouponsRepository couponsRepository, CartRepository cartRepository) {
        this.couponsRepository = couponsRepository;
        this.cartRepository = cartRepository;
    }

    @Transactional
    public Coupon createCoupon(Coupon coupon){
        validCoupon(coupon);
        return couponsRepository.save(coupon);
    }

    public Coupon findById(Long coupon){
        return couponsRepository.findById(coupon).orElseThrow(()->new NoSuchElementException("Coupon with id " + coupon + " not found"));
    }

    @Transactional
    public Coupon updateCoupon(Long couponId,Coupon updateCoupon){
        validCoupon(updateCoupon);
        Optional<Coupon> exitingCoupon=couponsRepository.findById(couponId);
        if(exitingCoupon.isPresent()){
            Coupon coupon=exitingCoupon.get();
            coupon.setCode(updateCoupon.getCode());
            coupon.setDiscount_value(updateCoupon.getDiscount_value());
            coupon.setDiscountType(updateCoupon.getDiscountType());
            coupon.setUsageLimit(updateCoupon.getUsageLimit());
            coupon.setUsedCount(updateCoupon.getUsedCount());
            coupon.setValid_to(updateCoupon.getValid_to());
            return couponsRepository.save(coupon);
        }
        throw new NoSuchElementException("Coupon with id " + couponId + " not found");
    }

    @Transactional
    public void removeCoupon(Long couponId){
        Coupon coupon=findById(couponId);
        couponsRepository.delete(coupon);
    }

    public void validCoupon(Coupon coupon) {
        if (coupon == null) {
            throw new IllegalArgumentException("Coupon must not be null");
        }

        if (coupon.getCode() == null || coupon.getCode().isBlank()) {
            throw new IllegalArgumentException("Coupon code must not be empty");
        }

        if (coupon.getDiscount_value() == null || coupon.getDiscount_value().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Discount value must be greater than zero");
        }

        if (coupon.getDiscountType() == null) {
            throw new IllegalArgumentException("Discount type must be specified");
        }

        if (coupon.getValid_to() != null && coupon.getValid_to().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Coupon expiration date must be in the future");
        }
    }
}
