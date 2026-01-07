package org.sopt.snappinserver.api.product.controller;

import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.api.product.code.ProductSuccessCode;
import org.sopt.snappinserver.api.product.dto.response.ProductReviewsResponse;
import org.sopt.snappinserver.api.product.dto.response.ProductReviewsMetaResponse;
import org.sopt.snappinserver.domain.product.service.usecase.GetProductReviewsUseCase;
import org.sopt.snappinserver.domain.review.service.dto.response.ReviewPageResult;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@RestController
public class ProductController implements ProductApi {

    private final GetProductReviewsUseCase getProductReviewsUseCase;

    @Override
    public ApiResponseBody<ProductReviewsResponse, ProductReviewsMetaResponse>
    getProductReviews(Long productId, Long cursor) {

        ReviewPageResult result =
            getProductReviewsUseCase.getProductReviews(productId, cursor);

        ProductReviewsResponse data =
            ProductReviewsResponse.from(result);

        ProductReviewsMetaResponse meta =
            new ProductReviewsMetaResponse(
                result.getNextCursor(),
                result.isHasNext()
            );

        return ApiResponseBody.ok(
            ProductSuccessCode.GET_PRODUCT_REVIEWS_OK,
            data,
            meta
        );
    }
}

