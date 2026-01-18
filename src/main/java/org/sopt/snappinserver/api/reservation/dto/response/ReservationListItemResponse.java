package org.sopt.snappinserver.api.reservation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.snappinserver.domain.reservation.service.dto.response.GetReservationListItemResult;

@Schema(description = "예약 목록 단일 응답 DTO")
public record ReservationListItemResponse(

    @Schema(description = "예약 ID", example = "51")
    Long reservationId,

    @Schema(description = "예약 상태(진행 단계)", example = "RESERVATION_REQUESTED")
    String status,

    @Schema(description = "예약자명", example = "홍길동")
    String client,

    @Schema(description = "예약 생성 일시", example = "2026-01-12 15:00:02")
    String createdAt,

    @Schema(description = "예약 상품 정보")
    ReservationListProductResponse product
) {

    public static ReservationListItemResponse from(GetReservationListItemResult result) {
        return new ReservationListItemResponse(
            result.reservationId(),
            result.status(),
            result.client(),
            result.createdAt(),
            ReservationListProductResponse.from(result.product())
        );
    }
}
