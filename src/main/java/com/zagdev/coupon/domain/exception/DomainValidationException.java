package com.zagdev.coupon.domain.exception;

import java.util.List;

public class DomainValidationException extends RuntimeException {

    private final List<String> errors;

    public DomainValidationException(String message) {
        super(message);
        this.errors = List.of(message);
    }

    public DomainValidationException(List<String> errors) {
        super(errors == null || errors.isEmpty() ? "Domain validation error" : errors.getFirst());
        this.errors = errors == null ? List.of("Domain validation error") : List.copyOf(errors);
    }

    public List<String> getErrors() {
        return errors;
    }
}

