package org.sopt.snappinserver.api.portfolio.controller;

import static org.sopt.snappinserver.api.portfolio.code.PortfolioSuccessCode.GET_POPULAR_PORTFOLIOS_OK;

import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.api.portfolio.code.PortfolioSuccessCode;
import org.sopt.snappinserver.api.portfolio.dto.request.GetPortfolioListRequest;
import org.sopt.snappinserver.api.portfolio.dto.response.GetCurationResponse;
import org.sopt.snappinserver.api.portfolio.dto.response.GetPopularPortfolioListResponse;
import org.sopt.snappinserver.api.portfolio.dto.response.GetPortfolioDetailResponse;
import org.sopt.snappinserver.api.portfolio.dto.response.GetPortfolioListResponse;
import org.sopt.snappinserver.api.portfolio.dto.response.GetPortfolioMetaResponse;
import org.sopt.snappinserver.domain.auth.infra.jwt.CustomUserInfo;
import org.sopt.snappinserver.domain.portfolio.service.dto.request.GetPortfolioListQuery;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.GetCuratedPortfolioResult;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.GetPopularPortfolioListResult;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.GetPortfolioDetailResult;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.GetPortfolioListResult;
import org.sopt.snappinserver.domain.portfolio.service.usecase.GetCuratedPortfolioUseCase;
import org.sopt.snappinserver.domain.portfolio.service.usecase.GetPopularPortfolioListUseCase;
import org.sopt.snappinserver.domain.portfolio.service.usecase.GetPortfolioDetailUseCase;
import org.sopt.snappinserver.domain.portfolio.service.usecase.GetPortfolioListUseCase;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/portfolios")
@RequiredArgsConstructor
@RestController
public class PortfolioController implements PortfolioApi {

    private final GetPopularPortfolioListUseCase getPopularPortfolioListUseCase;
    private final GetPortfolioDetailUseCase getPortfolioDetailUseCase;
    private final GetCuratedPortfolioUseCase getCuratedPortfolioUseCase;
    private final GetPortfolioListUseCase getPortfolioListUseCase;

    @Override
    @GetMapping("/popular")
    public ApiResponseBody<GetPopularPortfolioListResponse, Void> getPopularPortfolios() {
        GetPopularPortfolioListResult result = getPopularPortfolioListUseCase
            .getPopularPortfolioList();
        GetPopularPortfolioListResponse response = GetPopularPortfolioListResponse.from(result);

        return ApiResponseBody.ok(GET_POPULAR_PORTFOLIOS_OK, response);
    }

    @Override
    @GetMapping("/{portfolioId}")
    public ApiResponseBody<GetPortfolioDetailResponse, Void> getPortfolioDetail(
        @AuthenticationPrincipal CustomUserInfo userInfo,
        @PathVariable Long portfolioId
    ) {
        Long userId = (userInfo != null) ? userInfo.userId() : null;
        GetPortfolioDetailResult result = getPortfolioDetailUseCase.findPortfolioDetail(
            userId,
            portfolioId
        );
        GetPortfolioDetailResponse response = GetPortfolioDetailResponse.from(result);

        return ApiResponseBody.ok(PortfolioSuccessCode.GET_PORTFOLIO_DETAIL_OK, response);
    }

    @Override
    @GetMapping("/recommendation")
    public ApiResponseBody<GetCurationResponse, Void> getCuratedPortfolios(
        @AuthenticationPrincipal CustomUserInfo userInfo
    ) {
        GetCuratedPortfolioResult result = getCuratedPortfolioUseCase.getCuratedPortfolio(
            userInfo.userId()
        );
        GetCurationResponse response = GetCurationResponse.from(result);

        return ApiResponseBody.ok(PortfolioSuccessCode.GET_CURATED_PORTFOLIO_OK, response);
    }

    @Override
    @GetMapping
    public ApiResponseBody<GetPortfolioListResponse, GetPortfolioMetaResponse> getPortfolioList(
        @ModelAttribute GetPortfolioListRequest request
    ) {
        GetPortfolioListQuery query = getQuery(request);
        GetPortfolioListResult result = getPortfolioListUseCase.getPortfolioList(query);
        GetPortfolioListResponse response = GetPortfolioListResponse.from(result);
        GetPortfolioMetaResponse meta = GetPortfolioMetaResponse.from(result.meta());

        return ApiResponseBody.ok(PortfolioSuccessCode.GET_PORTFOLIO_LIST_OK, response, meta);
    }

    private GetPortfolioListQuery getQuery(GetPortfolioListRequest request) {
        return new GetPortfolioListQuery(
            request.moodIds(),
            request.productId(),
            request.photographerId(),
            request.snapCategory(),
            request.placeId(),
            request.cursor()
        );
    }
}
