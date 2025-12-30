package com.zagdev.coupon.api;

import com.zagdev.coupon.application.CouponService;
import com.zagdev.coupon.domain.exception.AlreadyDeletedException;
import com.zagdev.coupon.domain.exception.DomainValidationException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Validation failed");
        pd.setType(URI.create("https://coupon.com/problems/validation"));
        pd.setDetail("Request validation failed");
        pd.setProperty("errors", fieldErrors(ex));
        pd.setProperty("path", request.getRequestURI());
        return pd;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Validation failed");
        pd.setType(URI.create("https://coupon.com/problems/validation"));
        pd.setDetail(ex.getMessage());
        pd.setProperty("path", request.getRequestURI());
        return pd;
    }

    @ExceptionHandler(DomainValidationException.class)
    public ProblemDetail handleDomainValidation(DomainValidationException ex, HttpServletRequest request) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Validation failed");
        pd.setType(URI.create("https://coupoun.com/problems/domain-validation"));
        pd.setDetail(ex.getMessage());
        pd.setProperty("errors", ex.getErrors());
        pd.setProperty("path", request.getRequestURI());
        return pd;
    }

    @ExceptionHandler(CouponService.CouponNotFoundException.class)
    public ProblemDetail handleNotFound(CouponService.CouponNotFoundException ex, HttpServletRequest request) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        pd.setTitle("Not Found");
        pd.setType(URI.create("https://coupon.com/problems/not-found"));
        pd.setDetail(ex.getMessage());
        pd.setProperty("path", request.getRequestURI());
        return pd;
    }

    @ExceptionHandler(AlreadyDeletedException.class)
    public ProblemDetail handleAlreadyDeleted(AlreadyDeletedException ex, HttpServletRequest request) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        pd.setTitle("Conflict");
        pd.setType(URI.create("https://coupon.com/problems/conflict"));
        pd.setDetail(ex.getMessage());
        pd.setProperty("path", request.getRequestURI());
        return pd;
    }

    private static Map<String, String> fieldErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            errors.put(fe.getField(), fe.getDefaultMessage());
        }
        return errors;
    }
}

