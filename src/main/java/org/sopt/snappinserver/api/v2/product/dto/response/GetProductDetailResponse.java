package org.sopt.snappinserver.api.v2.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import org.sopt.snappinserver.domain.product.service.dto.response.GetProductResult;

@Schema(description = "상품 상세 조회 응답 DTO")
public record GetProductDetailResponse(

    @Schema(description = "상품 ID")
    Long id,

    @Schema(description = "상품 이미지 목록")
    List<String> images,

    @Schema(description = "상품명")
    String title,

    @Schema(description = "좋아요 여부")
    boolean isLiked,

    @Schema(description = "좋아요 수")
    long likeCount,

    @Schema(description = "평균 별점")
    BigDecimal averageRate,

    @Schema(description = "리뷰 개수")
    long reviewCount,

    @Schema(description = "가격")
    int price,

    @Schema(description = "스냅 작가 응답 DTO")
    GetProductPhotographerInfoResponseV2 photographerInfo,

    @Schema(description = "상품 안내 정보 응답 DTO")
    GetProductInfoResponse productInfo
) {

    public static GetProductDetailResponse from(GetProductResult result) {
        BigDecimal rate = result.averageRate() == null
            ? BigDecimal.ZERO.setScale(1, RoundingMode.HALF_UP)
            : BigDecimal.valueOf(result.averageRate())
                .setScale(1, RoundingMode.HALF_UP);

        return new GetProductDetailResponse(
            result.id(),
            result.images(),
            result.title(),
            result.isLiked(),
            result.likeCount(),
            rate,
            result.reviewCount(),
            result.price(),
            GetProductPhotographerInfoResponseV2.from(result.photographerInfo()),
            GetProductInfoResponse.from(result.productInfo())
        );
    }
}
