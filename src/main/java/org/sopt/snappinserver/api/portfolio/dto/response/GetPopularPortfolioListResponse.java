package org.sopt.snappinserver.api.portfolio.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.GetPopularPortfolioListResult;

@Schema(description = "비로그인 시 인기 무드 기반 포폴 목록 추천 조회 응답 DTO")
public record GetPopularPortfolioListResponse(

    @Schema(description = "인기 무드 목록")
    List<String> popularMoods,

    @Schema(description = "관련 포트폴리오 목록")
    List<GetPopularPortfolioResponse> portfolios
) {

    public static GetPopularPortfolioListResponse from(GetPopularPortfolioListResult result) {
        return new GetPopularPortfolioListResponse(
            result.popularMoods(),
            result.portfolios().stream()
                .map(GetPopularPortfolioResponse::from)
                .toList()
        );
    }
}
