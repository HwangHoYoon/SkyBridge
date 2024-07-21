package com.skybridge.review.repository;

import com.skybridge.review.entity.ReviewDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewDetailRepository extends JpaRepository<ReviewDetail, Long> {
}