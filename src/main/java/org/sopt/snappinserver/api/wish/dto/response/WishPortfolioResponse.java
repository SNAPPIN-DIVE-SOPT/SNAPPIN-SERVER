package org.sopt.snappinserver.api.wish.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.snappinserver.domain.wish.service.dto.response.WishPortfolioResult;

public record WishPortfolioResponse(
    @Schema(description = "포트폴리오 아이디", example = "101")
    Long portfolioId,

    @Schema(
        description = "좋아요 상태(요청 처리 후). true: 좋아요 처리됨, false: 취소됨",
        example = "true"
    )
    Boolean liked
) {
    public static WishPortfolioResponse from(WishPortfolioResult result) {
        return new WishPortfolioResponse(result.portfolioId(), result.liked());
    }
}
