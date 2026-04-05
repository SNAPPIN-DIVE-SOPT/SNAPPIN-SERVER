package org.sopt.snappinserver.api.v2.portfolio.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.GetPortfolioCardResultV2;

@Schema(description = "포트폴리오 카드 조회 응답 DTO")
public record GetPortfolioCardResponseV2(

    @Schema(description = "포트폴리오 ID")
    Long id,

    @Schema(description = "포트폴리오 이미지 url")
    String imageUrl,

    @Schema(description = "좋아요 여부")
    boolean isLiked,

    @Schema(description = "좋아요 수")
    long likeCount
) {

    public static GetPortfolioCardResponseV2 from(GetPortfolioCardResultV2 result) {
        return new GetPortfolioCardResponseV2(
            result.id(),
            result.imageUrl(),
            result.isLiked(),
            result.likeCount()
        );
    }
}
