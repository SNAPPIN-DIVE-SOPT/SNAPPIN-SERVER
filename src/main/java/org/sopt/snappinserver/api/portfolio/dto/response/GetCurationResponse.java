package org.sopt.snappinserver.api.portfolio.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.GetCuratedPortfolioResult;

@Schema(description = "로그인 시 큐레이션 기반 포폴 추천 목록 조회")
public record GetCurationResponse(

    @Schema(description = "큐레이션된 무드 태그 목록")
    List<String> curatedMoods,

    @Schema(description = "추천 포트폴리오 목록")
    List<GetPortfolioResponse> portfolios
) {

    public static GetCurationResponse from(GetCuratedPortfolioResult result) {
        return new GetCurationResponse(
            result.curatedMoods(),
            result.portfolios().stream()
                .map(GetPortfolioResponse::from)
                .toList()
        );
    }
}
