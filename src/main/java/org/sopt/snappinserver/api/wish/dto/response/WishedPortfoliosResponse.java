package org.sopt.snappinserver.api.wish.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.sopt.snappinserver.domain.wish.service.dto.response.WishedPortfoliosResult;

@Schema(description = "위시 포트폴리오 목록 조회 응답 DTO")
public record WishedPortfoliosResponse(

    @Schema(description = "좋아요한 포트폴리오 목록")
    List<WishedPortfolioResponse> portfolios
) {

    public static WishedPortfoliosResponse from(WishedPortfoliosResult result) {
        return new WishedPortfoliosResponse(
            result.portfolios().stream().map(WishedPortfolioResponse::from).toList()
        );
    }
}
