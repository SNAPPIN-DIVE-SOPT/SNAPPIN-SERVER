package org.sopt.snappinserver.api.portfolio.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.GetPortfolioCardResult;

@Schema(description = "포트폴리오 카드 조회 응답 DTO")
public record GetPortfolioCardResponse(

    @Schema(description = "포트폴리오 ID")
    Long id,

    @Schema(description = "포트폴리오 이미지 url")
    String imageUrl
) {

    public static GetPortfolioCardResponse from(GetPortfolioCardResult result) {
        return new GetPortfolioCardResponse(
            result.id(),
            result.imageUrl()
        );
    }
}
