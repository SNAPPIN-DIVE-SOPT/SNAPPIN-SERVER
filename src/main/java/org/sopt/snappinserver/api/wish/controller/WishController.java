package org.sopt.snappinserver.api.wish.controller;

import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.api.wish.code.WishSuccessCode;
import org.sopt.snappinserver.api.wish.dto.request.WishPortfolioRequest;
import org.sopt.snappinserver.api.wish.dto.request.WishProductRequest;
import org.sopt.snappinserver.api.wish.dto.response.WishPortfolioResponse;
import org.sopt.snappinserver.api.wish.dto.response.WishProductResponse;
import org.sopt.snappinserver.domain.auth.infra.jwt.CustomUserInfo;
import org.sopt.snappinserver.domain.wish.service.dto.response.WishPortfolioResult;
import org.sopt.snappinserver.domain.wish.service.dto.response.WishProductResult;
import org.sopt.snappinserver.domain.wish.service.usecase.PostWishPortfolioUseCase;
import org.sopt.snappinserver.domain.wish.service.usecase.PostWishProductUseCase;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/wishes")
@RequiredArgsConstructor
@RestController
public class WishController implements WishApi {

    private final PostWishPortfolioUseCase postWishPortfolioUseCase;
    private final PostWishProductUseCase postWishProductUseCase;

    @Override
    @PostMapping("/portfolios")
    public ApiResponseBody<WishPortfolioResponse, Void> updateWishPortfolio(
        @AuthenticationPrincipal CustomUserInfo userInfo,
        WishPortfolioRequest request
    ) {
        WishPortfolioResult result = postWishPortfolioUseCase.togglePortfolioWish(
            userInfo.userId(),
            request.portfolioId()
        );
        WishPortfolioResponse response = WishPortfolioResponse.from(result);

        return ApiResponseBody.ok(decideSuccessCode(result), response);
    }

    private WishSuccessCode decideSuccessCode(WishPortfolioResult result) {
        return result.liked() ? WishSuccessCode.POST_WISH_LIKE_PORTFOLIO_OK
            : WishSuccessCode.POST_WISH_CANCEL_PORTFOLIO_OK;
    }

    @Override
    @PostMapping("/products")
    public ApiResponseBody<WishProductResponse, Void> updateWishProduct(
        @AuthenticationPrincipal CustomUserInfo userInfo,
        WishProductRequest request
    ) {
        WishProductResult result = postWishProductUseCase.toggleProductWish(
            userInfo.userId(),
            request.productId()
        );
        WishProductResponse response = WishProductResponse.from(result);

        return ApiResponseBody.ok(decideSuccessCode(result), response);
    }

    private WishSuccessCode decideSuccessCode(WishProductResult result) {
        return result.liked() ? WishSuccessCode.POST_WISH_LIKE_PRODUCT_OK
            : WishSuccessCode.POST_WISH_CANCEL_PRODUCT_OK;
    }
}
