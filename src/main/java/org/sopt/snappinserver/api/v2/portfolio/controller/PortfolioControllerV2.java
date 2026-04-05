package org.sopt.snappinserver.api.v2.portfolio.controller;

import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.api.v2.portfolio.dto.request.GetPortfolioListRequestV2;
import org.sopt.snappinserver.global.util.ParsedCursor;
import org.sopt.snappinserver.api.v2.portfolio.dto.response.GetPortfolioListResponseV2;
import org.sopt.snappinserver.api.v2.portfolio.dto.response.GetPortfolioMetaResponseV2;
import org.sopt.snappinserver.domain.auth.infra.jwt.CustomUserInfo;
import org.sopt.snappinserver.global.enums.SortType;
import org.sopt.snappinserver.domain.portfolio.service.dto.request.GetPortfolioListQueryV2;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.GetPortfolioListResultV2;
import org.sopt.snappinserver.domain.portfolio.service.usecase.GetPortfolioListUseCaseV2;
import org.sopt.snappinserver.global.response.code.portfolio.PortfolioSuccessCode;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v2/portfolios")
@RequiredArgsConstructor
@RestController
@Validated
public class PortfolioControllerV2 implements PortfolioApi {

    private final GetPortfolioListUseCaseV2 getPortfolioListUseCaseV2;

    @Override
    public ApiResponseBody<GetPortfolioListResponseV2, GetPortfolioMetaResponseV2> getPortfolioList(
        @AuthenticationPrincipal CustomUserInfo userInfo,
        GetPortfolioListRequestV2 request
    ) {
        Long userId = (userInfo != null) ? userInfo.userId() : null;
        GetPortfolioListQueryV2 query = toQuery(request, userId);
        GetPortfolioListResultV2 result = getPortfolioListUseCaseV2.getPortfolioList(query);

        return ApiResponseBody.ok(
            PortfolioSuccessCode.GET_PORTFOLIO_LIST_OK,
            GetPortfolioListResponseV2.from(result),
            GetPortfolioMetaResponseV2.from(result.meta())
        );
    }

    private GetPortfolioListQueryV2 toQuery(GetPortfolioListRequestV2 request, Long userId) {
        SortType sort = request.sort() == null ? SortType.RECOMMENDED : request.sort();
        ParsedCursor cursor = ParsedCursor.of(request.cursor(), sort);

        return new GetPortfolioListQueryV2(
            request.moodIds(),
            request.productId(),
            request.photographerId(),
            request.snapCategory(),
            request.placeId(),
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
