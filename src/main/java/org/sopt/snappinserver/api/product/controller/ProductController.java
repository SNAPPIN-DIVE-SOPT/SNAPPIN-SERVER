package org.sopt.snappinserver.api.product.controller;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.YearMonth;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.api.product.code.ProductSuccessCode;
import org.sopt.snappinserver.api.product.dto.response.ProductAvailableTimesResponse;
import org.sopt.snappinserver.api.product.dto.response.ProductClosedDatesResponse;
import org.sopt.snappinserver.api.product.dto.response.ProductPeopleRangeResponse;
import org.sopt.snappinserver.api.product.dto.response.ProductReviewsMetaResponse;
import org.sopt.snappinserver.api.product.dto.response.ProductReviewsResponse;
import org.sopt.snappinserver.domain.auth.infra.jwt.CustomUserInfo;
import org.sopt.snappinserver.domain.product.service.dto.response.ProductAvailableTimesResult;
import org.sopt.snappinserver.domain.product.service.dto.response.ProductClosedDatesResult;
import org.sopt.snappinserver.domain.product.service.dto.response.ProductPeopleRangeResult;
import org.sopt.snappinserver.domain.product.service.dto.response.ProductReviewPageResult;
import org.sopt.snappinserver.domain.product.service.usecase.GetProductAvailableTimesUseCase;
import org.sopt.snappinserver.domain.product.service.usecase.GetProductClosedDatesUseCase;
import org.sopt.snappinserver.domain.product.service.usecase.GetProductPeopleRangeUseCase;
import org.sopt.snappinserver.domain.product.service.usecase.GetProductReviewsUseCase;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@RestController
@Validated
public class ProductController implements ProductApi {

    private final GetProductReviewsUseCase getProductReviewsUseCase;
    private final GetProductPeopleRangeUseCase getProductPeopleRangeUseCase;
    private final GetProductClosedDatesUseCase getProductClosedDatesUseCase;
    private final GetProductAvailableTimesUseCase getProductAvailableTimesUseCase;

    @Override
    @GetMapping("/{productId}/reviews")
    public ApiResponseBody<ProductReviewsResponse, ProductReviewsMetaResponse> getProductReviews(
        @NotNull @PathVariable Long productId,
        @RequestParam(required = false) Long cursor
    ) {
        ProductReviewPageResult result =
            getProductReviewsUseCase.getProductReviews(productId, cursor);

        return ApiResponseBody.ok(
            ProductSuccessCode.GET_PRODUCT_REVIEWS_OK,
            ProductReviewsResponse.from(result),
            ProductReviewsMetaResponse.from(result)
        );
    }

    @Override
    @GetMapping("/{productId}/available/people-range")
    public ApiResponseBody<ProductPeopleRangeResponse, Void> getProductPeopleRange(
        @AuthenticationPrincipal CustomUserInfo principal,

        @NotNull @PathVariable Long productId
    ) {
        ProductPeopleRangeResult result =
            getProductPeopleRangeUseCase.getProductPeopleRange(productId);

        return ApiResponseBody.ok(
            ProductSuccessCode.GET_PRODUCT_PEOPLE_RANGE_OK,
            ProductPeopleRangeResponse.from(result)
        );
    }

    @Override
    @GetMapping("/{productId}/closed-dates")
    public ApiResponseBody<ProductClosedDatesResponse, Void> getProductClosedDates(
        @AuthenticationPrincipal CustomUserInfo principal,

        @NotNull @PathVariable Long productId,
        @NotBlank @RequestParam String date
    ) {
        YearMonth yearMonth = YearMonth.parse(date);

        ProductClosedDatesResult result =
            getProductClosedDatesUseCase.getProductClosedDates(productId, yearMonth);

        return ApiResponseBody.ok(
            ProductSuccessCode.GET_PRODUCT_AVAILABLE_DATE_OK,
            ProductClosedDatesResponse.from(result)
        );
    }

    @Override
    @GetMapping("/{productId}/available/times")
    public ApiResponseBody<ProductAvailableTimesResponse, Void> getProductAvailableTimes(
        @AuthenticationPrincipal CustomUserInfo principal,

        @NotNull @PathVariable Long productId,

        @NotNull @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate date
    ) {
        ProductAvailableTimesResult result =
            getProductAvailableTimesUseCase.getProductAvailableTimes(
                productId,
                date
            );

        return ApiResponseBody.ok(
            ProductSuccessCode.GET_PRODUCT_AVAILABLE_TIMES_OK,
            ProductAvailableTimesResponse.from(result)
        );
    }
}
