package org.sopt.snappinserver.api.v1.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.sopt.snappinserver.domain.product.service.dto.response.PopularMoodProductItemResult;

@Schema(description = "인기 무드 상품 목록 개별 상품 응답 DTO")
public record GetPopularMoodProductItemResponse(

    @Schema(description = "상품 ID")
    Long id,

    @Schema(description = "상품 대표 이미지")
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
    int price
) {

    public static GetPopularMoodProductItemResponse from(PopularMoodProductItemResult result) {
        BigDecimal rate = result.rate() == null
            ? BigDecimal.ZERO.setScale(1, RoundingMode.HALF_UP)
            : BigDecimal.valueOf(result.rate())
                .setScale(1, RoundingMode.HALF_UP);

        return new GetPopularMoodProductItemResponse(
            result.id(),
            result.imageUrl(),
            result.title(),
            rate,
            result.reviewCount(),
            result.photographer(),
            result.price()
        );
    }
}
