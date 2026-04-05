package org.sopt.snappinserver.api.v2.product.controller;

import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.api.v2.product.dto.response.GetProductDetailResponse;
import org.sopt.snappinserver.domain.auth.infra.jwt.CustomUserInfo;
import org.sopt.snappinserver.domain.product.service.dto.response.GetProductResult;
import org.sopt.snappinserver.domain.product.service.usecase.GetProductDetailUseCase;
import org.sopt.snappinserver.global.response.code.product.ProductSuccessCode;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v2/products")
@RequiredArgsConstructor
@RestController
@Validated
public class ProductControllerV2 implements ProductApi {

    private final GetProductDetailUseCase getProductDetailUseCase;

    @Override
    public ApiResponseBody<GetProductDetailResponse, Void> getProductDetail(
        @AuthenticationPrincipal CustomUserInfo userInfo,
        Long productId
    ) {
        Long userId = (userInfo != null) ? userInfo.userId() : null;
        GetProductResult result = getProductDetailUseCase.getProductDetail(userId, productId);
        GetProductDetailResponse response = GetProductDetailResponse.from(result);

        return ApiResponseBody.ok(ProductSuccessCode.GET_PRODUCT_DETAIL_OK, response);
    }
}
