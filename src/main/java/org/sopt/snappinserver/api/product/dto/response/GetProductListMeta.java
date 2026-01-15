package org.sopt.snappinserver.api.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.snappinserver.domain.product.service.dto.response.CursorMeta;

@Schema(description = "상품 목록 조회 응답 Meta DTO")
public record GetProductListMeta(

    @Schema(description = "다음 커서 값")
    Long nextCursor,

    @Schema(description = "다음 커서 존재 여부")
    Boolean hasNext
) {

    public static GetProductListMeta from(CursorMeta meta) {
        return new GetProductListMeta(
            meta.nextCursor(),
            meta.hasNext()
        );
    }
}
