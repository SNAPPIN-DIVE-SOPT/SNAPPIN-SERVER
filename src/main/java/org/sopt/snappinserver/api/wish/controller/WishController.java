package org.sopt.snappinserver.api.wish.controller;

import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.api.wish.code.WishSuccessCode;
import org.sopt.snappinserver.api.wish.dto.request.WishPortfolioRequest;
import org.sopt.snappinserver.api.wish.dto.response.WishPortfolioResponse;
import org.sopt.snappinserver.domain.auth.infra.jwt.CustomUserInfo;
import org.sopt.snappinserver.domain.wish.service.dto.response.WishPortfolioResult;
import org.sopt.snappinserver.domain.wish.service.usecase.PostWishPortfolioUseCase;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/wishes")
@RequiredArgsConstructor
@RestController
public class WishController implements WishApi {

    private final PostWishPortfolioUseCase postWishPortfolioUseCase;


    @Override
    public ApiResponseBody<WishPortfolioResponse, Void> wishPortfolio(
        @AuthenticationPrincipal CustomUserInfo userInfo,
        WishPortfolioRequest request
    ) {
        WishPortfolioResult result =
            postWishPortfolioUseCase.execute(
                userInfo.userId(),
                request.portfolioId()
            );

        WishPortfolioResponse response =
            WishPortfolioResponse.from(result);

        return ApiResponseBody.ok(
            resolveSuccessCode(result),
            response
        );
    }

    private WishSuccessCode resolveSuccessCode(WishPortfolioResult result) {
        return result.liked()
            ? WishSuccessCode.POST_WISH_LIKE_PORTFOLIO_OK
            : WishSuccessCode.POST_WISH_CANCEL_PORTFOLIO_OK;
    }
}
