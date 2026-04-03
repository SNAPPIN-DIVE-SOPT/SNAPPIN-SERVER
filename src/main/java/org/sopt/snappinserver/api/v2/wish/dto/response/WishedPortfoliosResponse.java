package org.sopt.snappinserver.api.v2.wish.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.sopt.snappinserver.api.v1.wish.dto.response.WishedPortfolioResponse;
import org.sopt.snappinserver.domain.wish.service.dto.response.WishedPortfoliosPageResult;

@Schema(description = "위시 포트폴리오 커서 기반 목록 응답 DTO")
public record WishedPortfoliosResponse(

    @Schema(description = "좋아요한 포트폴리오 목록")
    List<WishedPortfolioResponse> portfolios

) {
    public static WishedPortfoliosResponse from(WishedPortfoliosPageResult result) {
        return new WishedPortfoliosResponse(
            result.portfolios().stream()
                .map(WishedPortfolioResponse::from)
                .toList()
        );
    }
}
