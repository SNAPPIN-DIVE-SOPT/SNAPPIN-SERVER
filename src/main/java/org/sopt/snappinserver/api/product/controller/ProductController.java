package org.sopt.snappinserver.api.product.controller;

import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.api.category.code.CategorySuccessCode;
import org.sopt.snappinserver.api.product.code.ProductSuccessCode;
import org.sopt.snappinserver.api.product.dto.response.ProductReviewsCursorResponse;
import org.sopt.snappinserver.domain.product.service.GetProductReviewsUseCase;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@RestController
public class ProductController implements ProductApi {

    private final GetProductReviewsUseCase getProductReviewsUseCase;

    @Override
    public ApiResponseBody<ProductReviewsCursorResponse, Void> getProductReviews(
            Long productId,
            Long cursor
    ) {
        ProductReviewsCursorResponse response =
                getProductReviewsUseCase.getProductReviews(productId, cursor);

        return ApiResponseBody.ok(ProductSuccessCode.GET_PRODUCT_REVIEWS_OK, response);
    }
}

