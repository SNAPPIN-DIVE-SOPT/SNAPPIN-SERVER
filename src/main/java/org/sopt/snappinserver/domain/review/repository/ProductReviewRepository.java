package org.sopt.snappinserver.domain.review.repository;

import org.sopt.snappinserver.domain.review.domain.entity.Review;

/**
 * 상품 기반 리뷰 등록 전용
 * 실제 저장·조회는 ReviewRepository에 위임
 */
public interface ProductReviewRepository {

    Review save(Review review);
}
