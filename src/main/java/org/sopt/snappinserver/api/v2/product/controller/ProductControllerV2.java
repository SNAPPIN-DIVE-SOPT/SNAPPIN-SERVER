package org.sopt.snappinserver.api.v2.product.controller;

import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.api.v2.product.dto.request.GetProductListRequestV2;
import org.sopt.snappinserver.api.v2.product.dto.response.GetProductListResponseV2;
import org.sopt.snappinserver.api.v2.product.dto.response.GetProductMetaResponseV2;
import org.sopt.snappinserver.domain.auth.infra.jwt.CustomUserInfo;
import org.sopt.snappinserver.domain.product.service.dto.request.GetProductListQueryV2;
import org.sopt.snappinserver.domain.product.service.dto.response.GetProductListResultV2;
import org.sopt.snappinserver.domain.product.service.usecase.GetProductListUseCaseV2;
import org.sopt.snappinserver.global.enums.SortType;
import org.sopt.snappinserver.global.response.code.product.ProductSuccessCode;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.sopt.snappinserver.global.util.ParsedCursor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v2/products")
@RequiredArgsConstructor
@RestController
@Validated
public class ProductControllerV2 implements ProductApiV2 {

    private final GetProductListUseCaseV2 getProductListUseCaseV2;

    @Override
    public ApiResponseBody<GetProductListResponseV2, GetProductMetaResponseV2> getProductList(
        @AuthenticationPrincipal CustomUserInfo userInfo,
        GetProductListRequestV2 request
    ) {
        Long userId = (userInfo != null) ? userInfo.userId() : null;
        GetProductListQueryV2 query = toQuery(request, userId);
        GetProductListResultV2 result = getProductListUseCaseV2.getProductList(query);

        return ApiResponseBody.ok(
            ProductSuccessCode.GET_PRODUCT_LIST_OK,
            GetProductListResponseV2.from(result),
            GetProductMetaResponseV2.from(result.meta())
        );
    }

    private GetProductListQueryV2 toQuery(GetProductListRequestV2 request, Long userId) {
        SortType sort = request.sort() == null ? SortType.RECOMMENDED : request.sort();
        ParsedCursor cursor = ParsedCursor.of(request.cursor(), sort);

        return new GetProductListQueryV2(
            request.moodIds(),
            request.photographerId(),
            request.snapCategory(),
            request.placeId(),
            request.date(),
            request.peopleCount(),
            cursor.cursorId(),
            cursor.cursorLikeCount(),
            cursor.cursorAvgRating(),
            sort,
            request.minPrice(),
            request.maxPrice(),
            userId
        );
    }
}
