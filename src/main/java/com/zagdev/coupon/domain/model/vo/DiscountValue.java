package com.zagdev.coupon.domain.model.vo;

import com.zagdev.coupon.domain.exception.DomainValidationException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class DiscountValue {

    private static final BigDecimal MIN = new BigDecimal("0.5");

    private final BigDecimal value;

    private DiscountValue(BigDecimal value) {
        this.value = value;
    }

    public static DiscountValue of(BigDecimal value) {
        if (value == null) {
            throw new DomainValidationException("discountValue is required");
        }
        if (value.compareTo(MIN) < 0) {
            throw new DomainValidationException("discountValue must be >= 0.5");
        }

        BigDecimal normalized = value.stripTrailingZeros();
        if (normalized.scale() < 0) {
            normalized = normalized.setScale(0, RoundingMode.UNNECESSARY);
        }

        return new DiscountValue(normalized);
    }

    public BigDecimal value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DiscountValue that)) return false;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toPlainString();
    }
}
