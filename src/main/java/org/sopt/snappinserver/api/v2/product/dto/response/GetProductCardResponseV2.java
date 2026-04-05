package org.sopt.snappinserver.api.v2.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.sopt.snappinserver.domain.product.service.dto.response.GetProductCardResultV2;

@Schema(description = "상품 카드 조회 응답 DTO")
public record GetProductCardResponseV2(

    @Schema(description = "상품 ID")
    Long id,

    @Schema(description = "상품 썸네일 이미지 url")
    String imageUrl,

    @Schema(description = "좋아요 여부")
    boolean isLiked,

    @Schema(description = "좋아요 수")
    long likeCount,

    @Schema(description = "평균 별점")
    Double averageRating,

    @Schema(description = "상품 제목")
    String title,

    @Schema(description = "리뷰 수")
    long reviewCount,

    @Schema(description = "작가 닉네임")
    String photographer,

    @Schema(description = "가격")
    int price,

    @Schema(description = "무드 목록")
    List<String> moods
) {

    public static GetProductCardResponseV2 from(GetProductCardResultV2 result) {
        return new GetProductCardResponseV2(
            result.id(),
            result.imageUrl(),
            result.isLiked(),
            result.likeCount(),
            result.averageRating(),
            result.title(),
            result.reviewCount(),
            result.photographer(),
            result.price(),
            result.moods()
        );
    }
}
