package org.sopt.snappinserver.api.v1.reservation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import org.sopt.snappinserver.domain.reservation.service.dto.response.GetReservationListProductResult;

@Schema(description = "예약 목록 상품 응답 DTO")
public record ReservationListProductResponse(

    @Schema(description = "상품 ID", example = "501")
    Long id,

    @Schema(description = "상품 대표 이미지", example = "product/product_grad_12.jpg")
    String imageUrl,

    @Schema(description = "상품명", example = "한여름밤의 스냅")
    String title,

    @Schema(description = "평균 별점", example = "4.7")
    BigDecimal rate,

    @Schema(description = "리뷰 개수", example = "20")
    int reviewCount,

    @Schema(description = "상품 등록 작가명", example = "김작가")
    String photographer,

    @Schema(description = "상품 가격", example = "90000")
    int price,

    @Schema(description = "상품 무드 태그 목록")
    List<String> moods,

    @Schema(description = "리뷰 작성 여부", example = "true")
    boolean isReviewed
) {

    public static ReservationListProductResponse from(GetReservationListProductResult result) {
        BigDecimal rate = result.rate() == null
            ? BigDecimal.ZERO.setScale(1, RoundingMode.HALF_UP)
            : BigDecimal.valueOf(result.rate())
                .setScale(1, RoundingMode.HALF_UP);


        return new ReservationListProductResponse(
            result.id(),
            result.imageUrl(),
            result.title(),
            rate,
            result.reviewCount(),
            result.photographer(),
            result.price(),
            result.moods(),
            result.isReviewed()
        );
    }
}
