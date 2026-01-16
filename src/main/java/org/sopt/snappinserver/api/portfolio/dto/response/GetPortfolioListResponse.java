package org.sopt.snappinserver.api.portfolio.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.GetPortfolioListResult;

@Schema(description = "포트폴리오 목록 조회 응답 DTO")
public record GetPortfolioListResponse(

    @Schema(description = "포트폴리오 카드 목록")
    List<GetPortfolioCardResponse> portfolios
) {

    public static GetPortfolioListResponse from(GetPortfolioListResult result) {
        return new GetPortfolioListResponse(
            result.portfolios().stream()
                .map(GetPortfolioCardResponse::from)
                .toList()
        );
    }
}
