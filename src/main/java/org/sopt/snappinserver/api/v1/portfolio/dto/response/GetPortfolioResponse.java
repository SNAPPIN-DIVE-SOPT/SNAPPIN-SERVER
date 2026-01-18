package org.sopt.snappinserver.api.v1.portfolio.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.GetPortfolioResult;

@Schema(description = "포트폴리오 응답 DTO")
public record GetPortfolioResponse(

    @Schema(description = "포트폴리오 ID")
    Long id,

    @Schema(description = "포트폴리오 이미지 목록")
    List<GetImageResponse> images,

    @Schema(description = "포트폴리오 관련 무드")
    List<String> moods,

    @Schema(description = "작가명")
    String photographerName
) {

    public static GetPortfolioResponse from(GetPortfolioResult result) {
        return new GetPortfolioResponse(
            result.id(),
            result.images().stream()
                .map(GetImageResponse::from)
                .toList(),
            result.moods(),
            result.photographerName()
        );
    }
}
