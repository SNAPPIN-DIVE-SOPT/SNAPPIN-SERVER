package org.sopt.snappinserver.api.v1.reservation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.sopt.snappinserver.domain.reservation.service.dto.response.GetReservationDetailProductResult;

@Schema(description = "예약 상세 상품 정보 응답 DTO")
public record ReservationDetailProductResponse(

    @Schema(description = "상품 ID", example = "51")
    Long id,

    @Schema(description = "상품 대표 이미지", example = "https://example.com/product301_thumb.jpg")
    String imageUrl,

    @Schema(description = "상품명", example = "한여름밤의 스냅")
    String title,

    @Schema(description = "평균 별점", example = "4.7")
    Double rate,

    @Schema(description = "리뷰 개수", example = "20")
    int reviewCount,

    @Schema(description = "상품 등록 작가", example = "김작가")
    String photographer,

    @Schema(description = "상품 가격", example = "90000")
    int price,

    @Schema(description = "상품 무드 태그 목록")
    List<String> moods
) {

    public static ReservationDetailProductResponse from(GetReservationDetailProductResult result) {
        return new ReservationDetailProductResponse(
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
