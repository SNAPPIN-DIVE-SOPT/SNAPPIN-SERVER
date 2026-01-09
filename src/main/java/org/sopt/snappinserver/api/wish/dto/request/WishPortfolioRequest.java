package org.sopt.snappinserver.api.wish.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "포트폴리오 좋아요/취소 요청 DTO")
public record WishPortfolioRequest(
    @Schema(description = "좋아요/취소할 포트폴리오 아이디", example = "1")
    @NotNull(message = "portfolioId는 필수입니다.")
    Long portfolioId
) {

}
