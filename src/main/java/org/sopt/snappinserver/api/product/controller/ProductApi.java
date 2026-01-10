package org.sopt.snappinserver.api.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import org.sopt.snappinserver.api.product.dto.request.ProductReservationRequest;
import org.sopt.snappinserver.api.product.dto.response.ProductAvailableTimesResponse;
import org.sopt.snappinserver.api.product.dto.response.ProductClosedDatesResponse;
import org.sopt.snappinserver.api.product.dto.response.ProductPeopleRangeResponse;
import org.sopt.snappinserver.api.product.dto.response.ProductReservationResponse;
import org.sopt.snappinserver.api.product.dto.response.ProductReviewsMetaResponse;
import org.sopt.snappinserver.api.product.dto.response.ProductReviewsResponse;
import org.sopt.snappinserver.domain.auth.infra.jwt.CustomUserInfo;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "06 - Product", description = "상품 관련 API")
public interface ProductApi {

    @Operation(
        summary = "상품 리뷰 목록 조회",
        description = "커서 기반 페이지네이션 방식으로 상품 리뷰 목록을 조회합니다."
    )
    @GetMapping("/{productId}/reviews")
    ApiResponseBody<ProductReviewsResponse, ProductReviewsMetaResponse> getProductReviews(
        @Schema(description = "상품 아이디", example = "1")
        @PathVariable @NotNull Long productId,

        @Schema(description = "다음 페이지 조회를 위한 커서 값", example = "6")
        @RequestParam(value = "cursor", required = false) Long cursor
    );

    @Operation(
        summary = "촬영 가능 인원 수 조회",
        description = "예약 과정에서 상품의 촬영 가능 최대/최소 인원 수를 조회합니다."
    )
    @GetMapping("/{productId}/available/people-range")
    ApiResponseBody<ProductPeopleRangeResponse, Void> getProductPeopleRange(
        @Parameter(hidden = true)
        CustomUserInfo principal,

        @Schema(description = "상품 아이디", example = "1")
        @PathVariable @NotNull Long productId
    );

    @Operation(
        summary = "달별 휴무일 목록 조회",
        description = "상품 예약 과정에서 달별 휴무일을 조회합니다."
    )
    @GetMapping("/{productId}/closed-dates")
    ApiResponseBody<ProductClosedDatesResponse, Void> getProductClosedDates(
        @Parameter(hidden = true)
        CustomUserInfo principal,

        @Schema(description = "상품 아이디", example = "1")
        @PathVariable @NotNull Long productId,

        @Schema(description = "조회할 연/월 (yyyy-MM)", example = "2026-03", required = true)
        @RequestParam(value = "date") @NotBlank String date
    );

    @Operation(
        summary = "시간대별 예약 가능 여부 조회",
        description = "상품 예약 과정에서 각 일자의 시간대별 예약 가능 여부를 조회합니다."
    )
    @GetMapping("/{productId}/available/times")
    ApiResponseBody<ProductAvailableTimesResponse, Void> getProductAvailableTimes(
        @Parameter(hidden = true)
        CustomUserInfo principal,

        @Schema(description = "상품 아이디", example = "1")
        @PathVariable @NotNull Long productId,

        @Schema(description = "조회할 날짜 (yyyy-MM-dd)", example = "2026-03-15", required = true)
        @RequestParam(value = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        @NotNull LocalDate date
    );


    @Operation(
        summary = "예약하기",
        description = "고객이 선택한 정보를 바탕으로 예약을 요청합니다."
    )
    @PostMapping("/{productId}/reservations")
    ApiResponseBody<ProductReservationResponse, Void> createProductReservation(
        @Parameter(hidden = true)
        CustomUserInfo principal,

        @Schema(description = "상품 아이디", example = "1")
        @PathVariable @NotNull Long productId,

        @RequestBody @Valid @NotNull ProductReservationRequest request
    );

}
