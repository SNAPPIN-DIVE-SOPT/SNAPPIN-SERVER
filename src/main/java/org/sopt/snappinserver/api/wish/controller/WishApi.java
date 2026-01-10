package org.sopt.snappinserver.api.wish.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sopt.snappinserver.api.wish.dto.request.WishPortfolioRequest;
import org.sopt.snappinserver.api.wish.dto.request.WishProductRequest;
import org.sopt.snappinserver.api.wish.dto.response.WishPortfolioResponse;
import org.sopt.snappinserver.api.wish.dto.response.WishProductResponse;
import org.sopt.snappinserver.domain.auth.infra.jwt.CustomUserInfo;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "012 - Wish", description = "좋아요 관련 API")
public interface WishApi {

    @Operation(summary = "포트폴리오 좋아요/취소", description = "로그인한 사용자가 포트폴리오에 대해 좋아요를 추가하거나, 이미 좋아요를 누른 경우 취소합니다.")
    ApiResponseBody<WishPortfolioResponse, Void> updateWishPortfolio(
        @Parameter(hidden = true)
        @AuthenticationPrincipal CustomUserInfo userInfo,
        @Valid @RequestBody WishPortfolioRequest request);

    @Operation(summary = "상품 좋아요/취소", description = "로그인한 사용자가 상품에 대해 좋아요를 추가하거나, 이미 좋아요를 누른 경우 취소합니다.")
    ApiResponseBody<WishProductResponse, Void> updateWishProduct(
        @Parameter(hidden = true)
        @AuthenticationPrincipal CustomUserInfo userInfo,
        @Valid @RequestBody WishProductRequest request);
}
