package org.sopt.snappinserver.api.product.controller;

import java.time.YearMonth;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.api.product.code.ProductSuccessCode;
import org.sopt.snappinserver.api.product.dto.response.ProductClosedDatesResponse;
import org.sopt.snappinserver.api.product.dto.response.ProductPeopleRangeResponse;
import org.sopt.snappinserver.api.product.dto.response.ProductReviewsResponse;
import org.sopt.snappinserver.api.product.dto.response.ProductReviewsMetaResponse;
import org.sopt.snappinserver.domain.auth.infra.jwt.CustomUserInfo;
import org.sopt.snappinserver.domain.product.service.dto.response.ProductClosedDatesResult;
import org.sopt.snappinserver.domain.product.service.dto.response.ProductPeopleRangeResult;
import org.sopt.snappinserver.domain.product.service.usecase.GetProductClosedDatesUseCase;
import org.sopt.snappinserver.domain.product.service.usecase.GetProductPeopleRangeUseCase;
import org.sopt.snappinserver.domain.product.service.usecase.GetProductReviewsUseCase;
import org.sopt.snappinserver.domain.product.service.dto.response.ProductReviewPageResult;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@RestController
public class ProductController implements ProductApi {

    private final GetProductReviewsUseCase getProductReviewsUseCase;
    private final GetProductPeopleRangeUseCase getProductPeopleRangeUseCase;
    private final GetProductClosedDatesUseCase getProductClosedDatesUseCase;

    @Override
    public ApiResponseBody<ProductReviewsResponse, ProductReviewsMetaResponse> getProductReviews(Long productId, Long cursor) {
        ProductReviewPageResult result = getProductReviewsUseCase.getProductReviews(productId, cursor);
        ProductReviewsResponse data = ProductReviewsResponse.from(result);
        ProductReviewsMetaResponse meta = ProductReviewsMetaResponse.from(result);

        return ApiResponseBody.ok(ProductSuccessCode.GET_PRODUCT_REVIEWS_OK, data, meta);
    }

    @Override
    public ApiResponseBody<ProductPeopleRangeResponse, Void> getProductPeopleRange(
        @AuthenticationPrincipal CustomUserInfo principal,
        Long productId
    ) {
        ProductPeopleRangeResult result = getProductPeopleRangeUseCase.getProductPeopleRange(productId);
        ProductPeopleRangeResponse response = ProductPeopleRangeResponse.from(result);

        return ApiResponseBody.ok(ProductSuccessCode.GET_PRODUCT_PEOPLE_RANGE_OK, response);
    }

    @Override
    public ApiResponseBody<ProductClosedDatesResponse, Void> getProductClosedDates(
        @AuthenticationPrincipal CustomUserInfo principal,
        Long productId,
        String date
    ) {
        YearMonth yearMonth = YearMonth.parse(date);

        ProductClosedDatesResult result = getProductClosedDatesUseCase.getProductClosedDates(productId, yearMonth);
        ProductClosedDatesResponse response = ProductClosedDatesResponse.from(result);

        return ApiResponseBody.ok(ProductSuccessCode.GET_PRODUCT_AVAILABLE_DATE_OK, response);
    }

}

