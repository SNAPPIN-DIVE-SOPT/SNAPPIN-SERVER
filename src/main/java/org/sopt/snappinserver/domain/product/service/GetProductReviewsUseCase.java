package org.sopt.snappinserver.domain.product.service;

import org.sopt.snappinserver.api.product.dto.response.ProductReviewsCursorResponse;

public interface GetProductReviewsUseCase {
    ProductReviewsCursorResponse getProductReviews(
            Long productId,
            Long cursor
    );
}
