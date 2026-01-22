package org.sopt.snappinserver.api.v1.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import org.sopt.snappinserver.domain.product.service.dto.response.GetProductCardResult;

@Schema(description = "상품 목록 조회 개별 상품 응답 DTO")
public record GetProductCardResponse(

    @Schema(description = "상품 ID")
    Long id,

    @Schema(description = "상품 썸네일")
    String imageUrl,

    @Schema(description = "상품명")
    String title,

    @Schema(description = "평균 별점")
    BigDecimal rate,

    @Schema(description = "리뷰 개수")
    long reviewCount,

    @Schema(description = "상품 등록 작가")
    String photographer,

    @Schema(description = "상품 가격")
    int price,

    @Schema(description = "상품 무드 태그 목록")
    List<String> moods
) {

    public static GetProductCardResponse from(GetProductCardResult result) {
        return new GetProductCardResponse(
            result.id(),
            result.imageUrl(),
            result.title(),
            BigDecimal.valueOf(result.rate())
                .setScale(1, RoundingMode.HALF_UP),
            result.reviewCount(),
            result.photographer(),
            result.price(),
            result.moods()
        );
    }
}
