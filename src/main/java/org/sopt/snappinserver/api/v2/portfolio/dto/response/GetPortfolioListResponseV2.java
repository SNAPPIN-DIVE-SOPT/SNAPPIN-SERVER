package org.sopt.snappinserver.api.v2.portfolio.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.GetPortfolioListResultV2;

@Schema(description = "포트폴리오 목록 조회 응답 DTO")
public record GetPortfolioListResponseV2(

    @Schema(description = "포트폴리오 카드 목록")
    List<GetPortfolioCardResponseV2> portfolios
) {

    public static GetPortfolioListResponseV2 from(GetPortfolioListResultV2 result) {
        return new GetPortfolioListResponseV2(
            result.portfolios().stream()
                .map(GetPortfolioCardResponseV2::from)
                .toList()
        );
    }
}
