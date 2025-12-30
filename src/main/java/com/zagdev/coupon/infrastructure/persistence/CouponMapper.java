package com.zagdev.coupon.infrastructure.persistence;

import com.zagdev.coupon.domain.model.Coupon;
import com.zagdev.coupon.domain.model.vo.CouponCode;
import com.zagdev.coupon.domain.model.vo.DiscountValue;

public final class CouponMapper {

    private CouponMapper() {
    }

    public static CouponEntity toEntity(Coupon domain) {
        CouponEntity e = new CouponEntity();
        e.setId(domain.getId());
        e.setCode(domain.getCode().value());
        e.setDescription(domain.getDescription());
        e.setDiscountValue(domain.getDiscountValue().value());
        e.setExpirationDate(domain.getExpirationDate());
        e.setPublished(domain.isPublished());
        e.setRedeemed(domain.isRedeemed());
        e.setStatus(domain.getStatus());
        e.setDeletedAt(domain.getDeletedAt());
        return e;
    }

    public static Coupon toDomain(CouponEntity e) {
        return Coupon.rehydrate(
                e.getId(),
                CouponCode.of(e.getCode()),
                e.getDescription(),
                DiscountValue.of(e.getDiscountValue()),
                e.getExpirationDate(),
                e.isPublished(),
                e.isRedeemed(),
                e.getStatus(),
                e.getDeletedAt()
        );
    }
}
