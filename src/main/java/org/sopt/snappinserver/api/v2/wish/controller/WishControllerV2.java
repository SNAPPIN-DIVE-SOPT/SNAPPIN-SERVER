package org.sopt.snappinserver.api.v2.wish.controller;

import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.api.v2.wish.dto.response.WishedProductsMetaResponse;
import org.sopt.snappinserver.api.v2.wish.dto.response.WishedProductsResponse;
import org.sopt.snappinserver.domain.auth.infra.jwt.CustomUserInfo;
import org.sopt.snappinserver.domain.wish.service.dto.response.WishedProductsPageResult;
import org.sopt.snappinserver.domain.wish.service.usecase.GetWishedProductsUseCase;
import org.sopt.snappinserver.global.response.code.wish.WishSuccessCode;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v2/wishes")
@RequiredArgsConstructor
@RestController
@Validated
public class WishControllerV2 implements WishApi {

    private final GetWishedProductsUseCase getWishedProductsUseCase;

    @Override
    public ApiResponseBody<WishedProductsResponse, WishedProductsMetaResponse> getWishedProducts(
        @AuthenticationPrincipal CustomUserInfo userInfo,
        Long cursor
    ) {
        WishedProductsPageResult result =
            getWishedProductsUseCase.getWishedProductsPage(userInfo.userId(), cursor);

        return ApiResponseBody.ok(
            WishSuccessCode.GET_WISHED_PRODUCTS_OK,
            WishedProductsResponse.from(result),
            WishedProductsMetaResponse.from(result)
        );
    }
}
