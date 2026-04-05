package org.sopt.snappinserver.api.v1.wish.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sopt.snappinserver.api.v1.wish.dto.request.WishPortfolioRequest;
import org.sopt.snappinserver.api.v1.wish.dto.request.WishProductRequest;
import org.sopt.snappinserver.api.v1.wish.dto.response.WishPortfolioResponse;
import org.sopt.snappinserver.api.v1.wish.dto.response.WishProductResponse;
import org.sopt.snappinserver.api.v1.wish.dto.response.WishedPortfoliosResponse;
import org.sopt.snappinserver.api.v1.wish.dto.response.WishedProductsResponse;
import org.sopt.snappinserver.domain.auth.infra.jwt.CustomUserInfo;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "012 - Wish", description = "좋아요 관련 API")
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

    @Deprecated
    @Operation(
        summary = "위시 포트폴리오 목록 조회",
        description = "v2 API로 대체되었습니다. /api/v2/wishes/portfolios로 대체해서 사용하세요.",
        deprecated = true
    )
    @GetMapping("/portfolios")
    ApiResponseBody<WishedPortfoliosResponse, Void> getWishedPortfolios(

        @Parameter(hidden = true)
        CustomUserInfo userInfo
    );

    @Deprecated
    @Operation(
        summary = "위시 상품 목록 조회",
        description = "v2 API로 대체되었습니다. /api/v2/wishes/products로 대체해서 사용하세요.",
        deprecated = true
    )
    @GetMapping("/products")
    ApiResponseBody<WishedProductsResponse, Void> getWishedProducts(

        @Parameter(hidden = true)
        CustomUserInfo userInfo
    );

}
