package org.sopt.snappinserver.api.v2.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.snappinserver.domain.product.service.dto.response.GetProductListMetaV2;

@Schema(description = "상품 목록 조회 메타 정보")
public record GetProductMetaResponseV2(

    @Schema(description = "다음 커서 값")
    String nextCursor,

    @Schema(description = "다음 커서 존재 여부")
    boolean hasNext,

    @Schema(description = "전체 검색 결과 수")
    long totalCount
) {

    public static GetProductMetaResponseV2 from(GetProductListMetaV2 meta) {
        return new GetProductMetaResponseV2(meta.nextCursor(), meta.hasNext(), meta.totalCount());
    }
}
