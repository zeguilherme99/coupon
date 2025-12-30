package com.zagdev.coupon.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CreateCouponRequest(
        @NotBlank(message = "code is required")
        String code,

        @NotBlank(message = "description is required")
        String description,

        @NotNull(message = "expirationDate is required")
        Instant expirationDate,

        Boolean published,

        @NotNull(message = "discountValue is required")
        BigDecimal discountValue
) {
}

