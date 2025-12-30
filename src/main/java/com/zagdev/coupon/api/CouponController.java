package com.zagdev.coupon.api;

import com.zagdev.coupon.api.dto.CouponResponse;
import com.zagdev.coupon.api.dto.CreateCouponRequest;
import com.zagdev.coupon.application.CouponService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/coupon")
@Tag(name = "Coupon")
public class CouponController {

    private final CouponService service;

    public CouponController(CouponService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(
            summary = "Create coupon",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Created"),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema())),
            }
    )
    public ResponseEntity<CouponResponse> create(@RequestBody @Valid CreateCouponRequest request) {
        var created = service.create(
                request.code(),
                request.description(),
                request.discountValue(),
                request.expirationDate(),
                request.published()
        );
        return ResponseEntity
                .created(URI.create("/coupon/" + created.getId()))
                .body(CouponResponse.from(created));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get coupon by id (includes DELETED coupons)",
            description = "GET /coupon/{id} still returns 200 with status=DELETED.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema())),
            }
    )
    public CouponResponse get(@PathVariable String id) {
        return CouponResponse.from(service.get(UUID.fromString(id)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Soft delete coupon",
            responses = {
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema())),
                    @ApiResponse(responseCode = "409", description = "Conflict", content = @Content(schema = @Schema()))
            }
    )
    public void delete(@PathVariable String id) {
        service.delete(UUID.fromString(id));
    }
}
