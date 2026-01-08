package org.sopt.snappinserver.api.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sopt.snappinserver.api.product.dto.response.ProductPeopleRangeResponse;
import org.sopt.snappinserver.api.product.dto.response.ProductReviewsResponse;
import org.sopt.snappinserver.api.product.dto.response.ProductReviewsMetaResponse;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "06 - Product", description = "상품 관련 API")
public interface ProductApi {

    @Operation(
        summary = "상품 리뷰 목록 조회",
        description = "커서 기반 페이지네이션 방식으로 상품 리뷰 목록을 조회합니다."
    )
    @GetMapping("/{productId}/reviews")
    ApiResponseBody<ProductReviewsResponse, ProductReviewsMetaResponse>
    getProductReviews(
        @PathVariable Long productId,
        @RequestParam(required = false) Long cursor
    );

    @Operation(
        summary = "촬영 가능 인원 수 조회",
        description = "예약 과정에서 상품의 촬영 가능 최대/최소 인원 수를 조회합니다."
    )
    @GetMapping("/{productId}/available/people-range")
    ApiResponseBody<ProductPeopleRangeResponse, Void>
    getProductPeopleRange(
        @PathVariable Long productId
    );

}
