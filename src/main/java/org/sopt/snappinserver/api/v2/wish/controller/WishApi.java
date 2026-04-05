package org.sopt.snappinserver.api.v2.wish.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sopt.snappinserver.api.v2.wish.dto.response.WishedPortfoliosMetaResponse;
import org.sopt.snappinserver.api.v2.wish.dto.response.WishedPortfoliosResponse;
import org.sopt.snappinserver.api.v2.wish.dto.response.WishedProductsMetaResponse;
import org.sopt.snappinserver.api.v2.wish.dto.response.WishedProductsResponse;
import org.sopt.snappinserver.domain.auth.infra.jwt.CustomUserInfo;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "012 - Wish", description = "좋아요 관련 API")
public interface WishApi {

    @Operation(
        summary = "위시 포트폴리오 목록 조회",
        description = "커서 기반 페이지네이션으로 사용자가 좋아요한 포트폴리오 목록을 조회합니다. 페이지 크기는 10입니다."
    )
    @GetMapping("/portfolios")
    ApiResponseBody<WishedPortfoliosResponse, WishedPortfoliosMetaResponse> getWishedPortfolios(

        @Parameter(hidden = true)
        CustomUserInfo userInfo,

        @Schema(description = "다음 페이지 조회를 위한 커서 값", example = "11", nullable = true)
        @RequestParam(value = "cursor", required = false) Long cursor
    );

    @Operation(
        summary = "위시 상품 목록 조회",
        description = "커서 기반 페이지네이션으로 사용자가 좋아요한 상품 목록을 조회합니다. 페이지 크기는 10입니다."
    )
    @GetMapping("/products")
    ApiResponseBody<WishedProductsResponse, WishedProductsMetaResponse> getWishedProducts(

        @Parameter(hidden = true)
        CustomUserInfo userInfo,

        @Schema(description = "다음 페이지 조회를 위한 커서 값", example = "11", nullable = true)
        @RequestParam(value = "cursor", required = false) Long cursor
    );

}
