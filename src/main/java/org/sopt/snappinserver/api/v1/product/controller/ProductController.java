package org.sopt.snappinserver.api.v1.product.controller;

import java.time.LocalDate;
import java.time.YearMonth;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.api.v1.product.dto.response.ProductDurationTimeResponse;
import org.sopt.snappinserver.domain.product.service.dto.response.ProductDurationTimeResult;
import org.sopt.snappinserver.domain.product.service.usecase.GetProductDurationTimeUseCase;
import org.sopt.snappinserver.global.response.code.product.ProductSuccessCode;
import org.sopt.snappinserver.api.v1.product.dto.request.CreateProductReviewRequest;
import org.sopt.snappinserver.api.v1.product.dto.request.ProductReservationRequest;
import org.sopt.snappinserver.api.v1.product.dto.response.GetPopularMoodProductsResponse;
import org.sopt.snappinserver.api.v1.product.dto.response.GetProductExtraInfoResponse;
import org.sopt.snappinserver.api.v1.product.dto.response.GetProductDetailResponse;
import org.sopt.snappinserver.api.v1.product.dto.response.GetProductListMeta;
import org.sopt.snappinserver.api.v1.product.dto.response.GetProductListResponse;
import org.sopt.snappinserver.api.v1.product.dto.response.ProductAvailableTimesResponse;
import org.sopt.snappinserver.api.v1.product.dto.response.ProductClosedDatesResponse;
import org.sopt.snappinserver.api.v1.product.dto.response.ProductPeopleRangeResponse;
import org.sopt.snappinserver.api.v1.product.dto.response.CreateProductReviewResponse;
import org.sopt.snappinserver.api.v1.product.dto.response.ProductReservationResponse;
import org.sopt.snappinserver.api.v1.product.dto.response.ProductReviewsMetaResponse;
import org.sopt.snappinserver.api.v1.product.dto.response.ProductReviewsResponse;
import org.sopt.snappinserver.domain.auth.infra.jwt.CustomUserInfo;
import org.sopt.snappinserver.domain.product.service.dto.request.GetProductListQuery;
import org.sopt.snappinserver.domain.product.service.dto.request.CreateProductReviewCommand;
import org.sopt.snappinserver.domain.product.service.dto.request.ProductReservationCommand;
import org.sopt.snappinserver.domain.product.service.dto.response.GetPopularMoodProductsResult;
import org.sopt.snappinserver.domain.product.service.dto.response.GetProductExtraInfoResult;
import org.sopt.snappinserver.domain.product.service.dto.response.GetProductListResult;
import org.sopt.snappinserver.domain.product.service.dto.response.GetProductResult;
import org.sopt.snappinserver.domain.product.service.dto.response.ProductAvailableTimesResult;
import org.sopt.snappinserver.domain.product.service.dto.response.ProductClosedDatesResult;
import org.sopt.snappinserver.domain.product.service.dto.response.ProductPeopleRangeResult;
import org.sopt.snappinserver.domain.product.service.dto.response.CreateProductReviewResult;
import org.sopt.snappinserver.domain.product.service.dto.response.ProductReservationResult;
import org.sopt.snappinserver.domain.product.service.dto.response.ProductReviewPageResult;
import org.sopt.snappinserver.domain.product.service.usecase.GetProductAvailableTimesUseCase;
import org.sopt.snappinserver.domain.product.service.usecase.GetProductClosedDatesUseCase;
import org.sopt.snappinserver.domain.product.service.usecase.GetProductDetailUseCase;
import org.sopt.snappinserver.domain.product.service.usecase.GetPopularMoodProductsUseCase;
import org.sopt.snappinserver.domain.product.service.usecase.GetProductExtraInfoUseCase;
import org.sopt.snappinserver.domain.product.service.usecase.GetProductListUseCase;
import org.sopt.snappinserver.domain.product.service.usecase.GetProductPeopleRangeUseCase;
import org.sopt.snappinserver.domain.product.service.usecase.GetProductReviewsUseCase;
import org.sopt.snappinserver.domain.product.service.usecase.PostProductReservationUseCase;
import org.sopt.snappinserver.domain.product.service.usecase.PostProductReviewUseCase;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
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
    private final PostProductReviewUseCase postProductReviewUseCase;
    private final PostProductReservationUseCase postProductReservationUseCase;
    private final GetProductDetailUseCase getProductDetailUseCase;
    private final GetProductListUseCase getProductListUseCase;
    private final GetPopularMoodProductsUseCase getPopularMoodProductsUseCase;
    private final GetProductExtraInfoUseCase getProductExtraInfoUseCase;
    private final GetProductDurationTimeUseCase getProductDurationTimeUseCase;

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
    public ApiResponseBody<CreateProductReviewResponse, Void> createProductReview(
        @AuthenticationPrincipal CustomUserInfo userInfo,
        Long productId,
        CreateProductReviewRequest request
    ) {
        CreateProductReviewCommand command = new CreateProductReviewCommand(
            userInfo.userId(),
            productId,
            request.rating(),
            request.content(),
            request.imageUrls()
        );

        CreateProductReviewResult result = postProductReviewUseCase.createProductReview(command);

        return ApiResponseBody.ok(
            ProductSuccessCode.POST_PRODUCT_REVIEW_CREATED,
            CreateProductReviewResponse.from(result)
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
    public ApiResponseBody<ProductDurationTimeResponse, Void> getProductDurationTime(
        @AuthenticationPrincipal CustomUserInfo principal,
        Long productId
    ) {
        ProductDurationTimeResult result =
            getProductDurationTimeUseCase.getProductDurationTime(productId);

        return ApiResponseBody.ok(
            ProductSuccessCode.GET_PRODUCT_DURATION_TIME_OK,
            ProductDurationTimeResponse.from(result)
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
        GetProductListQuery query
    ) {
        GetProductListResult result = getProductListUseCase.getProductList(query);
        GetProductListResponse response = GetProductListResponse.from(result);
        GetProductListMeta meta = GetProductListMeta.from(result.cursorMeta());

        return ApiResponseBody.ok(ProductSuccessCode.GET_PRODUCT_LIST_OK, response, meta);
    }

    @Override
    public ApiResponseBody<GetPopularMoodProductsResponse, Void> getPopularMoodProducts(
        @AuthenticationPrincipal CustomUserInfo principal,
        Long moodId
    ) {
        Long userId = principal != null ? principal.userId() : null;
        GetPopularMoodProductsResult result =
            getPopularMoodProductsUseCase.getPopularMoodProducts(moodId, userId);

        return ApiResponseBody.ok(
            ProductSuccessCode.GET_POPULAR_MOOD_PRODUCTS_OK,
            GetPopularMoodProductsResponse.from(result)
        );
    }

    @Override
    public ApiResponseBody<GetProductExtraInfoResponse, Void> getProductExtraInfo(
        @AuthenticationPrincipal CustomUserInfo principal,
        Long productId
    ) {
        GetProductExtraInfoResult result = getProductExtraInfoUseCase.getProductExtraInfo(productId);

        return ApiResponseBody.ok(
            ProductSuccessCode.GET_PRODUCT_EXTRA_INFO_OK,
            GetProductExtraInfoResponse.from(result)
        );
    }

}
