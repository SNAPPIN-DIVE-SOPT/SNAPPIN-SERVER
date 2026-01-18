package org.sopt.snappinserver.api.v1.portfolio.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.GetPopularPortfolioResult;

@Schema(description = "인기 무드 기반 추천 포트폴리오 응답 DTO")
public record GetPopularPortfolioResponse(

    @Schema(description = "포트폴리오 ID")
    Long id,

    @Schema(description = "포트폴리오 이미지 목록")
    List<GetImageResponse> images,

    @Schema(description = "포트폴리오 무드 목록")
    List<String> moods,

    @Schema(description = "포트폴리오 작가명")
    String photographerName
) {

    public static GetPopularPortfolioResponse from(GetPopularPortfolioResult result) {
        return new GetPopularPortfolioResponse(
            result.id(),
            result.images().stream()
                .map(GetImageResponse::from)
                .toList(),
            result.moods(),
            result.photographerName()
        );
    }
}
