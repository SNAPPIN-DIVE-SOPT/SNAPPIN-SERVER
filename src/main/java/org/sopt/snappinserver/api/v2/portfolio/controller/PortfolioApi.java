package org.sopt.snappinserver.api.v2.portfolio.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sopt.snappinserver.api.v2.portfolio.dto.request.GetPortfolioListRequestV2;
import org.sopt.snappinserver.api.v2.portfolio.dto.response.GetPortfolioListResponseV2;
import org.sopt.snappinserver.api.v2.portfolio.dto.response.GetPortfolioMetaResponseV2;
import org.sopt.snappinserver.domain.auth.infra.jwt.CustomUserInfo;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Tag(name = "08 - Portfolio", description = "포트폴리오 관련 API")
public interface PortfolioApi {

    @Operation(
        summary = "포폴 목록 조회 (전체조회/필터링/검색) API",
        description = "포트폴리오 전체 조회, 필터링, 검색 시 사용되는 API입니다. isLiked, likeCount, sort, minPrice, maxPrice가 추가되었습니다."
    )
    @GetMapping
    ApiResponseBody<GetPortfolioListResponseV2, GetPortfolioMetaResponseV2> getPortfolioList(
        @Parameter(hidden = true)
        CustomUserInfo userInfo,

        @Valid @ModelAttribute GetPortfolioListRequestV2 request
    );
}
