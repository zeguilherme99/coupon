package com.zagdev.coupon.domain.model;

import com.zagdev.coupon.domain.exception.AlreadyDeletedException;
import com.zagdev.coupon.domain.exception.DomainValidationException;
import com.zagdev.coupon.domain.model.vo.CouponCode;
import com.zagdev.coupon.domain.model.vo.DiscountValue;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Coupon {

    private final UUID id;
    private final CouponCode code;
    private final String description;
    private final DiscountValue discountValue;
    private final Instant expirationDate;
    private final boolean published;
    private final boolean redeemed;
    private final CouponStatus status;
    private final Instant deletedAt;

    private Coupon(UUID id,
                   CouponCode code,
                   String description,
                   DiscountValue discountValue,
                   Instant expirationDate,
                   boolean published,
                   boolean redeemed,
                   CouponStatus status,
                   Instant deletedAt) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.discountValue = discountValue;
        this.expirationDate = expirationDate;
        this.published = published;
        this.redeemed = redeemed;
        this.status = status;
        this.deletedAt = deletedAt;
    }

    public static Coupon rehydrate(UUID id,
                                   CouponCode code,
                                   String description,
                                   DiscountValue discountValue,
                                   Instant expirationDate,
                                   boolean published,
                                   boolean redeemed,
                                   CouponStatus status,
                                   Instant deletedAt) {
        return new Coupon(id, code, description, discountValue, expirationDate, published, redeemed, status, deletedAt);
    }

    public static Coupon create(String rawCode,
                                String description,
                                BigDecimal discountValue,
                                Instant expirationDate,
                                Boolean published,
                                Clock clock) {

        if (rawCode == null || rawCode.isBlank()) {
            throw new DomainValidationException("code is required");
        }
        if (description == null || description.isBlank()) {
            throw new DomainValidationException("description is required");
        }
        if (discountValue == null) {
            throw new DomainValidationException("discountValue is required");
        }
        if (expirationDate == null) {
            throw new DomainValidationException("expirationDate is required");
        }

        Clock c = clock == null ? Clock.systemUTC() : clock;
        Instant now = Instant.now(c);
        if (expirationDate.isBefore(now)) {
            throw new DomainValidationException("expirationDate must not be in the past");
        }

        CouponCode code = CouponCode.of(rawCode);
        DiscountValue dv = DiscountValue.of(discountValue);

        return new Coupon(
                UUID.randomUUID(),
                code,
                description,
                dv,
                expirationDate,
                published != null && published,
                false,
                CouponStatus.ACTIVE,
                null
        );
    }

    public Coupon softDelete(Clock clock) {
        if (this.status == CouponStatus.DELETED) {
            throw new AlreadyDeletedException("coupon already deleted");
        }
        Instant now = Instant.now(clock == null ? Clock.systemUTC() : clock);
        return new Coupon(
                this.id,
                this.code,
                this.description,
                this.discountValue,
                this.expirationDate,
                this.published,
                this.redeemed,
                CouponStatus.DELETED,
                now
        );
    }

    public UUID getId() {
        return id;
    }

    public CouponCode getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public DiscountValue getDiscountValue() {
        return discountValue;
    }

    public Instant getExpirationDate() {
        return expirationDate;
    }

    public boolean isPublished() {
        return published;
    }

    public boolean isRedeemed() {
        return redeemed;
    }

    public CouponStatus getStatus() {
        return status;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coupon coupon)) return false;
        return Objects.equals(id, coupon.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
