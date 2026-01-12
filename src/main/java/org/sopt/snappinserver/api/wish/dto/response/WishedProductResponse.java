package org.sopt.snappinserver.api.wish.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.sopt.snappinserver.domain.wish.service.dto.response.WishedProductResult;

@Schema(description = "위시 상품 단일 응답 DTO")
public record WishedProductResponse(

    @Schema(description = "상품 아이디", example = "1")
    Long id,

    @Schema(description = "상품 대표 이미지 URL", example = "https://example.com/product1.jpg")
    String imageUrl,

    @Schema(description = "상품명", example = "인물 스냅 촬영")
    String title,

    @Schema(description = "평균 별점", example = "4.8")
    Double rate,

    @Schema(description = "리뷰 개수", example = "23")
    Integer reviewCount,

    @Schema(description = "상품 등록 작가", example = "김사진")
    String photographer,

    @Schema(description = "상품 가격", example = "150000")
    Integer price,

    @Schema(description = "상품 무드 태그 목록", example = "[\"따뜻한\", \"자연스러운\", \"투명한\"]")
    List<String> moods
) {

    public static WishedProductResponse from(WishedProductResult result) {
        return new WishedProductResponse(
            result.id(),
            result.imageUrl(),
            result.title(),
            result.rate(),
            result.reviewCount(),
            result.photographer(),
            result.price(),
            result.moods()
        );
    }
}
