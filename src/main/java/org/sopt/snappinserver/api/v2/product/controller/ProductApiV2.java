package org.sopt.snappinserver.api.v2.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sopt.snappinserver.api.v2.product.dto.request.GetProductListRequestV2;
import org.sopt.snappinserver.api.v2.product.dto.response.GetProductListResponseV2;
import org.sopt.snappinserver.api.v2.product.dto.response.GetProductMetaResponseV2;
import org.sopt.snappinserver.domain.auth.infra.jwt.CustomUserInfo;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Tag(name = "06 - Product", description = "상품 관련 API")
public interface ProductApiV2 {

    @Operation(
        summary = "상품 목록 조회 API v2",
        description = "상품 전체 조회, 필터링, 검색 시 사용되는 API입니다. isLiked, likeCount, sort, minPrice, maxPrice가 추가되었습니다."
    )
    @GetMapping
    ApiResponseBody<GetProductListResponseV2, GetProductMetaResponseV2> getProductList(
        @Parameter(hidden = true)
        CustomUserInfo userInfo,

        @Valid @ModelAttribute GetProductListRequestV2 request
    );
}
