package org.sopt.snappinserver.api.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.snappinserver.domain.product.service.dto.response.ProductReservationResult;

@Schema(description = "상품 예약 응답 DTO")
public record ProductReservationResponse(

    @Schema(description = "생성된 예약 ID", example = "501") Long reservationId,

    @Schema(description = "예약 상태", example = "RESERVATION_REQUESTED") String status
) {

    public static ProductReservationResponse from(ProductReservationResult result) {
        return new ProductReservationResponse(result.reservationId(), result.status().name());
    }
}
