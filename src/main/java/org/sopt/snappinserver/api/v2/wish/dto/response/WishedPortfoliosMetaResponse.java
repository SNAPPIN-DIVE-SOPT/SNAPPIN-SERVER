package org.sopt.snappinserver.api.v2.wish.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.snappinserver.domain.wish.service.dto.response.WishedPortfoliosPageResult;

@Schema(description = "위시 포트폴리오 커서 기반 조회 메타 정보 DTO")
public record WishedPortfoliosMetaResponse(

    @Schema(description = "다음 페이지 조회를 위한 커서 값", example = "11", nullable = true)
    Long nextCursor,

    @Schema(description = "다음 페이지 존재 여부", example = "true")
    boolean hasNext

) {
    public static WishedPortfoliosMetaResponse from(WishedPortfoliosPageResult result) {
        return new WishedPortfoliosMetaResponse(
            result.nextCursor(),
            result.hasNext()
        );
    }
}
