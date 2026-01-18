package org.sopt.snappinserver.api.v1.product.controller;

import java.time.LocalDate;
import java.time.YearMonth;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.api.v1.product.code.ProductSuccessCode;
import org.sopt.snappinserver.api.v1.product.dto.request.ProductReservationRequest;
import org.sopt.snappinserver.api.v1.product.dto.response.GetProductDetailResponse;
import org.sopt.snappinserver.api.v1.product.dto.response.GetProductListMeta;
import org.sopt.snappinserver.api.v1.product.dto.response.GetProductListResponse;
import org.sopt.snappinserver.api.v1.product.dto.response.ProductAvailableTimesResponse;
import org.sopt.snappinserver.api.v1.product.dto.response.ProductClosedDatesResponse;
import org.sopt.snappinserver.api.v1.product.dto.response.ProductPeopleRangeResponse;
import org.sopt.snappinserver.api.v1.product.dto.response.ProductPriceResponse;
import org.sopt.snappinserver.api.v1.product.dto.response.ProductReservationResponse;
import org.sopt.snappinserver.api.v1.product.dto.response.ProductReviewsMetaResponse;
import org.sopt.snappinserver.api.v1.product.dto.response.ProductReviewsResponse;
import org.sopt.snappinserver.domain.auth.infra.jwt.CustomUserInfo;
import org.sopt.snappinserver.domain.product.service.dto.request.GetProductListQuery;
import org.sopt.snappinserver.domain.product.service.dto.request.ProductReservationCommand;
import org.sopt.snappinserver.domain.product.service.dto.response.GetProductListResult;
import org.sopt.snappinserver.domain.product.service.dto.response.GetProductResult;
import org.sopt.snappinserver.domain.product.service.dto.response.ProductAvailableTimesResult;
import org.sopt.snappinserver.domain.product.service.dto.response.ProductClosedDatesResult;
import org.sopt.snappinserver.domain.product.service.dto.response.ProductPeopleRangeResult;
import org.sopt.snappinserver.domain.product.service.dto.response.ProductPriceResult;
import org.sopt.snappinserver.domain.product.service.dto.response.ProductReservationResult;
import org.sopt.snappinserver.domain.product.service.dto.response.ProductReviewPageResult;
import org.sopt.snappinserver.domain.product.service.usecase.GetProductAvailableTimesUseCase;
import org.sopt.snappinserver.domain.product.service.usecase.GetProductClosedDatesUseCase;
import org.sopt.snappinserver.domain.product.service.usecase.GetProductDetailUseCase;
import org.sopt.snappinserver.domain.product.service.usecase.GetProductListUseCase;
import org.sopt.snappinserver.domain.product.service.usecase.GetProductPeopleRangeUseCase;
import org.sopt.snappinserver.domain.product.service.usecase.GetProductPriceUseCase;
import org.sopt.snappinserver.domain.product.service.usecase.GetProductReviewsUseCase;
import org.sopt.snappinserver.domain.product.service.usecase.PostProductReservationUseCase;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
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
    private final PostProductReservationUseCase postProductReservationUseCase;
    private final GetProductDetailUseCase getProductDetailUseCase;
    private final GetProductListUseCase getProductListUseCase;
    private final GetProductPriceUseCase getProductPriceUseCase;

    @Override
    public ApiResponseBody<ProductReviewsResponse, ProductReviewsMetaResponse> getProductReviews(
        Long productId,
        Long cursor
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
    public ApiResponseBody<ProductPeopleRangeResponse, Void> getProductPeopleRange(
        @AuthenticationPrincipal CustomUserInfo principal,
        Long productId
    ) {
        ProductPeopleRangeResult result =
            getProductPeopleRangeUseCase.getProductPeopleRange(productId);

        return ApiResponseBody.ok(
            ProductSuccessCode.GET_PRODUCT_PEOPLE_RANGE_OK,
            ProductPeopleRangeResponse.from(result)
        );
    }

    @Override
    public ApiResponseBody<ProductClosedDatesResponse, Void> getProductClosedDates(
        @AuthenticationPrincipal CustomUserInfo principal,
        Long productId,
        String date
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
    public ApiResponseBody<ProductAvailableTimesResponse, Void> getProductAvailableTimes(
        @AuthenticationPrincipal CustomUserInfo principal,
        Long productId,
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

    @Override
    public ApiResponseBody<ProductReservationResponse, Void> createProductReservation(
        @AuthenticationPrincipal CustomUserInfo principal,
        Long productId,
        ProductReservationRequest request
    ) {
        ProductReservationResult result =
            postProductReservationUseCase.reserve(
                productId,
                principal.userId(),
                ProductReservationCommand.from(request)
            );

        return ApiResponseBody.ok(
            ProductSuccessCode.POST_PRODUCT_RESERVATION_OK,
            ProductReservationResponse.from(result)
        );
    }

    @Override
    public ApiResponseBody<GetProductDetailResponse, Void> getProductDetail(
        @AuthenticationPrincipal CustomUserInfo userInfo,
        Long productId
    ) {
        Long userId = (userInfo != null) ? userInfo.userId() : null;
        GetProductResult result = getProductDetailUseCase.getProductDetail(userId, productId);
        GetProductDetailResponse response = GetProductDetailResponse.from(result);

        return ApiResponseBody.ok(ProductSuccessCode.GET_PRODUCT_DETAIL_OK, response);
    }

    @Override
    public ApiResponseBody<GetProductListResponse, GetProductListMeta> getProductList(
        @ModelAttribute GetProductListQuery query
    ) {
        GetProductListResult result = getProductListUseCase.getProductList(query);
        GetProductListResponse response = GetProductListResponse.from(result);
        GetProductListMeta meta = GetProductListMeta.from(result.cursorMeta());

        return ApiResponseBody.ok(ProductSuccessCode.GET_PRODUCT_LIST_OK, response, meta);
    }

    @Override
    public ApiResponseBody<ProductPriceResponse, Void> getProductPrice(
        @AuthenticationPrincipal CustomUserInfo userInfo,
        Long productId
    ) {
        ProductPriceResult result = getProductPriceUseCase.getProductPrice(productId);

        return ApiResponseBody.ok(
            ProductSuccessCode.GET_PRODUCT_PRICE_OK,
            ProductPriceResponse.from(result)
        );
    }
}
