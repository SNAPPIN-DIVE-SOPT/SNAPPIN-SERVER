package org.sopt.snappinserver.api.v2.wish.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.snappinserver.domain.wish.service.dto.response.WishedPortfolioResult;

@Schema(description = "위시 포트폴리오 단일 응답 DTO")
public record WishedPortfolioResponse(

    @Schema(description = "포트폴리오 아이디", example = "1")
    Long id,

    @Schema(
        description = "포트폴리오 대표 이미지 URL",
        example = "https://example.com/portfolio1.jpg"
    )
    String imageUrl,

    @Schema(description = "포트폴리오 좋아요 수", example = "3")
    Integer likeCount
) {

    public static WishedPortfolioResponse from(WishedPortfolioResult result) {
        return new WishedPortfolioResponse(
            result.id(),
            result.imageUrl(),
            result.likeCount()
        );
    }
}
