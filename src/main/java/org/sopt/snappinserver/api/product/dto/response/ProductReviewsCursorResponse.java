package org.sopt.snappinserver.api.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Schema(description = "상품 리뷰 커서 기반 목록 응답 DTO")
public class ProductReviewsCursorResponse {
    @Schema(description = "상품 리뷰 목록")
    private List<ProductReviewResponse> reviews;

    @Schema(description = "다음 커서 값", example = "89", nullable = true)
    private Long nextCursor;

    @Schema(description = "다음 페이지 존재 여부", example = "true")
    private boolean hasNext;
}
