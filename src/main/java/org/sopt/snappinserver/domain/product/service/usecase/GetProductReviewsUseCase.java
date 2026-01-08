package org.sopt.snappinserver.domain.product.service.usecase;

import org.sopt.snappinserver.domain.product.service.dto.response.ProductReviewPageResult;

public interface GetProductReviewsUseCase {

    ProductReviewPageResult getProductReviews(
        Long productId,
        Long cursor
    );
}
