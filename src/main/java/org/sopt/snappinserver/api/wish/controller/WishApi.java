package org.sopt.snappinserver.api.wish.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sopt.snappinserver.api.wish.dto.request.WishPortfolioRequest;
import org.sopt.snappinserver.api.wish.dto.request.WishProductRequest;
import org.sopt.snappinserver.api.wish.dto.response.WishPortfolioResponse;
import org.sopt.snappinserver.api.wish.dto.response.WishProductResponse;
import org.sopt.snappinserver.api.wish.dto.response.WishedPortfoliosResponse;
import org.sopt.snappinserver.api.wish.dto.response.WishedProductsResponse;
import org.sopt.snappinserver.domain.auth.infra.jwt.CustomUserInfo;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "012 - Wish", description = "좋아요 관련 API")
@Validated
public interface WishApi {

    @Operation(
        summary = "포트폴리오 좋아요/취소",
        description = "로그인한 사용자가 포트폴리오에 대해 좋아요를 추가하거나, 이미 좋아요를 누른 경우 취소합니다."
    )
    @PostMapping("/portfolios")
    ApiResponseBody<WishPortfolioResponse, Void> updateWishPortfolio(

        @Parameter(hidden = true)
        CustomUserInfo userInfo,

        @Valid @RequestBody WishPortfolioRequest request
    );

    @Operation(
        summary = "상품 좋아요/취소",
        description = "로그인한 사용자가 상품에 대해 좋아요를 추가하거나, 이미 좋아요를 누른 경우 취소합니다."
    )
    @PostMapping("/products")
    ApiResponseBody<WishProductResponse, Void> updateWishProduct(

        @Parameter(hidden = true)
        CustomUserInfo userInfo,

        @Valid @RequestBody WishProductRequest request
    );

    @Operation(
        summary = "위시 포트폴리오 목록 조회",
        description = "사용자가 좋아요한 전체 포트폴리오 목록을 조회합니다."
    )
    @GetMapping("/portfolios")
    ApiResponseBody<WishedPortfoliosResponse, Void> getWishedPortfolios(

        @Parameter(hidden = true)
        CustomUserInfo userInfo
    );

    @Operation(
        summary = "위시 상품 목록 조회",
        description = "사용자가 좋아요한 전체 상품 목록을 조회합니다."
    )
    @GetMapping("/products")
    ApiResponseBody<WishedProductsResponse, Void> getWishedProducts(

        @Parameter(hidden = true)
        CustomUserInfo userInfo
    );

}
