package org.sopt.snappinserver.api.wish.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.snappinserver.domain.wish.service.dto.response.WishPortfolioResult;

@Schema(description = "포트폴리오 좋아요/취소 응답 DTO")
public record WishPortfolioResponse(
    @Schema(description = "포트폴리오 아이디", example = "101")
    Long portfolioId,

    @Schema(description = "좋아요 상태 (요청 처리 후)", example = "true")
    boolean liked
) {

    public static WishPortfolioResponse from(WishPortfolioResult result) {
        return new WishPortfolioResponse(result.portfolioId(), Boolean.TRUE.equals(result.liked()));
    }
}
