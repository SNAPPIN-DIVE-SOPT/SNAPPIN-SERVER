package org.sopt.snappinserver.api.v1.portfolio.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sopt.snappinserver.api.v1.portfolio.dto.request.GetPortfolioListRequest;
import org.sopt.snappinserver.api.v1.portfolio.dto.response.GetCurationResponse;
import org.sopt.snappinserver.api.v1.portfolio.dto.response.GetPopularPortfolioListResponse;
import org.sopt.snappinserver.api.v1.portfolio.dto.response.GetPortfolioDetailResponse;
import org.sopt.snappinserver.api.v1.portfolio.dto.response.GetPortfolioListResponse;
import org.sopt.snappinserver.api.v1.portfolio.dto.response.GetPortfolioMetaResponse;
import org.sopt.snappinserver.domain.auth.infra.jwt.CustomUserInfo;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "08 - Portfolio", description = "포트폴리오 관련 API")
public interface PortfolioApi {

    @Operation(
        summary = "비로그인 시 인기 무드 기반 포폴 추천 목록 조회 API",
        description = "비로그인 시 인기 무드와 많이 매칭되는 순서대로 속한 포트폴리오를 3개 조회합니다."
    )
    ApiResponseBody<GetPopularPortfolioListResponse, Void> getPopularPortfolios();

    @Operation(
        summary = "포트폴리오 상세 조회 API",
        description = "포트폴리오 ID를 받아서 포트폴리오 상세 정보를 조회합니다."
    )
    ApiResponseBody<GetPortfolioDetailResponse, Void> getPortfolioDetail(
        @Parameter(hidden = true)
        CustomUserInfo userInfo,

        @PathVariable Long portfolioId
    );

    @Operation(
        summary = "로그인 시 큐레이션 기반 포폴 추천 목록 조회",
        description = "큐레이션 기반 포트폴리오 추천 목록을 조회합니다."
    )
    ApiResponseBody<GetCurationResponse, Void> getCuratedPortfolios(
        @Parameter(hidden = true)
        CustomUserInfo userInfo
    );

    @Operation(
        summary = "포폴 목록 조회 (전체조회/필터링(무드&상품)/검색) API",
        description = "포트폴리오 전체 조회, 필터링, 검색 시 사용되는 API 입니다."
    )
    ApiResponseBody<GetPortfolioListResponse, GetPortfolioMetaResponse> getPortfolioList(
        @Valid @ModelAttribute GetPortfolioListRequest request
    );
}
