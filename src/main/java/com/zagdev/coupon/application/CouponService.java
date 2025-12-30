package com.zagdev.coupon.application;

import com.zagdev.coupon.domain.model.Coupon;
import com.zagdev.coupon.infrastructure.persistence.CouponEntity;
import com.zagdev.coupon.infrastructure.persistence.CouponJpaRepository;
import com.zagdev.coupon.infrastructure.persistence.CouponMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Service
public class CouponService {

    private final CouponJpaRepository repository;
    private final Clock clock;

    public CouponService(CouponJpaRepository repository, Clock clock) {
        this.repository = repository;
        this.clock = clock;
    }

    @Transactional
    public Coupon create(String code,
                         String description,
                         java.math.BigDecimal discountValue,
                         Instant expirationDate,
                         Boolean published) {
        Coupon coupon = Coupon.create(code, description, discountValue, expirationDate, published, clock);
        CouponEntity saved = repository.save(CouponMapper.toEntity(coupon));
        return CouponMapper.toDomain(saved);
    }

    @Transactional(readOnly = true)
    public Coupon get(UUID id) {
        return repository.findById(id)
                .map(CouponMapper::toDomain)
                .orElseThrow(() -> new CouponNotFoundException("coupon not found"));
    }

    @Transactional
    public void delete(UUID id) {
        CouponEntity entity = repository.findById(id)
                .orElseThrow(() -> new CouponNotFoundException("coupon not found"));

        Coupon domain = CouponMapper.toDomain(entity);
        Coupon deleted = domain.softDelete(clock);

        repository.save(CouponMapper.toEntity(deleted));
    }

    public static class CouponNotFoundException extends RuntimeException {
        public CouponNotFoundException(String message) {
            super(message);
        }
    }
}
