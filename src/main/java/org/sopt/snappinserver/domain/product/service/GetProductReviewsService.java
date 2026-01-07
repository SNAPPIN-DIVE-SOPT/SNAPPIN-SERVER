package org.sopt.snappinserver.domain.product.service;

import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.api.product.dto.response.ProductReviewResponse;
import org.sopt.snappinserver.api.product.dto.response.ProductReviewsCursorResponse;
import org.sopt.snappinserver.api.product.dto.response.ProductReviewsCursorResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetProductReviewsService implements GetProductReviewsUseCase {
    private static final int PAGE_SIZE = 5;

    @Override
    public ProductReviewsCursorResponse getProductReviews(
            Long productId,
            Long cursor
    ) {
        // TODO: Repository 연동 (커서 기반 조회)
        List<ProductReviewResponse> reviews = List.of();

        return new ProductReviewsCursorResponse(
                reviews,
                null,
                false
        );
    }
}
