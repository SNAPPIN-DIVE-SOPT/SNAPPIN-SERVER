package org.sopt.snappinserver.api.portfolio.controller;

import static org.sopt.snappinserver.api.portfolio.code.PortfolioSuccessCode.GET_POPULAR_PORTFOLIOS_OK;

import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.api.portfolio.dto.response.GetPopularPortfolioListResponse;
import org.sopt.snappinserver.api.portfolio.dto.response.GetPopularPortfolioResponse;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.GetPopularPortfolioListResult;
import org.sopt.snappinserver.domain.portfolio.service.usecase.GetPopularPortfolioListUseCase;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/portfolios")
@RequiredArgsConstructor
@RestController
public class PortfolioController {

    private final GetPopularPortfolioListUseCase getPopularPortfolioListUseCase;

    public ApiResponseBody<GetPopularPortfolioListResponse, Void> getPopularPortfolios() {
        GetPopularPortfolioListResult result = getPopularPortfolioListUseCase
            .getPopularPortfolioList();
        GetPopularPortfolioListResponse response = GetPopularPortfolioListResponse.from(result);

        return ApiResponseBody.ok(GET_POPULAR_PORTFOLIOS_OK, response);
    }
}
