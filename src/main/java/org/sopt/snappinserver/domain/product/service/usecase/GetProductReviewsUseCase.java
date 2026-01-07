package org.sopt.snappinserver.domain.product.service.usecase;

import org.sopt.snappinserver.domain.review.service.dto.response.ReviewPageResult;

public interface GetProductReviewsUseCase {

    ReviewPageResult getProductReviews(
        Long productId,
        Long cursor
    );
}
