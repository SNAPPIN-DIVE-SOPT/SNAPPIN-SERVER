package org.sopt.snappinserver.api.portfolio.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.GetPortfolioListMeta;

@Schema(description = "포트폴리오 목록 조회 메타 정보")
public record GetPortfolioMetaResponse(

    @Schema(description = "다음 커서 값")
    Long nextCursor,

    @Schema(description = "다음 커서 존재 여부")
    Boolean hasNext
) {

    public static GetPortfolioMetaResponse from(GetPortfolioListMeta result) {
        return new GetPortfolioMetaResponse(
            result.nextCursor(),
            result.hasNext()
        );
    }
}
