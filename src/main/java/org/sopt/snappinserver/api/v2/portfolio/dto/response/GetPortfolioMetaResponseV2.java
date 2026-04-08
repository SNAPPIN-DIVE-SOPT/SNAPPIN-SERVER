package org.sopt.snappinserver.api.v2.portfolio.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.GetPortfolioListMetaV2;

@Schema(description = "포트폴리오 목록 조회 메타 정보")
public record GetPortfolioMetaResponseV2(

    @Schema(description = "다음 커서 값")
    String nextCursor,

    @Schema(description = "다음 커서 존재 여부")
    boolean hasNext,

    @Schema(description = "전체 검색 결과 수")
    long totalCount
) {

    public static GetPortfolioMetaResponseV2 from(GetPortfolioListMetaV2 meta) {
        return new GetPortfolioMetaResponseV2(meta.nextCursor(), meta.hasNext(), meta.totalCount());
    }
}
