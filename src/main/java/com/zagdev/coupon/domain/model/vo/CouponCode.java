package com.zagdev.coupon.domain.model.vo;

import com.zagdev.coupon.domain.exception.DomainValidationException;

import java.util.Objects;

public final class CouponCode {

    private final String value;

    private CouponCode(String value) {
        this.value = value;
    }

    public static CouponCode of(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new DomainValidationException("code is required");
        }
        String sanitized = raw.replaceAll("[^A-Za-z0-9]", "").toUpperCase();
        if (sanitized.length() != 6) {
            throw new DomainValidationException("code must have exactly 6 alphanumeric characters after sanitization");
        }
        return new CouponCode(sanitized);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CouponCode that)) return false;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}

