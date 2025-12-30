package com.zagdev.coupon.domain;

import com.zagdev.coupon.domain.exception.AlreadyDeletedException;
import com.zagdev.coupon.domain.exception.DomainValidationException;
import com.zagdev.coupon.domain.model.Coupon;
import com.zagdev.coupon.domain.model.CouponStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.*;

class CouponDomainTests {

    private final Clock fixedClock = Clock.fixed(Instant.parse("2025-01-01T00:00:00Z"), ZoneOffset.UTC);

    @Test
    void codeSanitization_removesSpecialChars_andUppercases() {
        var c = Coupon.create("abC-12#3", "desc", new BigDecimal("0.5"), Instant.parse("2025-01-02T00:00:00Z"), false, fixedClock);
        assertThat(c.getCode().value()).isEqualTo("ABC123");
    }

    @Test
    void rejects_whenSanitizedLengthNotSix() {
        assertThatThrownBy(() -> Coupon.create("A-1", "desc", new BigDecimal("0.5"), Instant.parse("2025-01-02T00:00:00Z"), false, fixedClock))
                .isInstanceOf(DomainValidationException.class)
                .hasMessage("code must have exactly 6 alphanumeric characters after sanitization");
    }

    @Test
    void rejects_whenDiscountLessThanMin() {
        assertThatThrownBy(() -> Coupon.create("ABC123", "desc", new BigDecimal("0.49"), Instant.parse("2025-01-02T00:00:00Z"), false, fixedClock))
                .isInstanceOf(DomainValidationException.class)
                .hasMessage("discountValue must be >= 0.5");
    }

    @Test
    void rejects_whenExpirationInPast() {
        assertThatThrownBy(() -> Coupon.create("ABC123", "desc", new BigDecimal("0.5"), Instant.parse("2024-12-31T23:59:59Z"), false, fixedClock))
                .isInstanceOf(DomainValidationException.class)
                .hasMessage("expirationDate must not be in the past");
    }

    @Test
    void softDelete_changesStatusToDeleted() {
        var c = Coupon.create("ABC123", "desc", new BigDecimal("0.5"), Instant.parse("2025-01-02T00:00:00Z"), true, fixedClock);
        var deleted = c.softDelete(fixedClock);
        assertThat(deleted.getStatus()).isEqualTo(CouponStatus.DELETED);
        assertThat(deleted.getDeletedAt()).isNotNull();
    }

    @Test
    void softDelete_onDeleted_throwsAlreadyDeleted() {
        var c = Coupon.create("ABC123", "desc", new BigDecimal("0.5"), Instant.parse("2025-01-02T00:00:00Z"), true, fixedClock);
        var deleted = c.softDelete(fixedClock);
        assertThatThrownBy(() -> deleted.softDelete(fixedClock))
                .isInstanceOf(AlreadyDeletedException.class)
                .hasMessage("coupon already deleted");
    }
}

