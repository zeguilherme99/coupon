package com.zagdev.coupon.api.dto;

import com.zagdev.coupon.domain.model.Coupon;

import java.math.BigDecimal;
import java.time.Instant;

public record CouponResponse(
        String id,
        String code,
        String description,
        BigDecimal discountValue,
        Instant expirationDate,
        String status,
        boolean published,
        boolean redeemed
) {
    public static CouponResponse from(Coupon c) {
        return new CouponResponse(
                c.getId().toString(),
                c.getCode().value(),
                c.getDescription(),
                c.getDiscountValue().value(),
                c.getExpirationDate(),
                c.getStatus().name(),
                c.isPublished(),
                c.isRedeemed()
        );
    }
}

