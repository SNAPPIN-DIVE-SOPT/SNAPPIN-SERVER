package org.sopt.snappinserver.api.v1.reservation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.snappinserver.domain.reservation.service.dto.response.GetReservationDetailResult;

@Schema(description = "예약 상세 전체 응답 DTO")
public record ReservationDetailResponse(

    @Schema(description = "예약 상태(진행 단계)", example = "RESERVATION_REQUESTED")
    String status,

    @Schema(description = "예약 상품 정보")
    ReservationDetailProductResponse productInfo,

    @Schema(description = "예약 상세 정보")
    ReservationDetailInfoResponse reservationInfo,

    @Schema(description = "결제 정보")
    ReservationDetailPaymentResponse paymentInfo,

    @Schema(description = "리뷰 정보")
    ReservationDetailReviewResponse reviewInfo
) {
    public static ReservationDetailResponse from(GetReservationDetailResult result) {
        return new ReservationDetailResponse(
            result.status().name(),
            ReservationDetailProductResponse.from(result.product()),
            ReservationDetailInfoResponse.from(result.reservationInfo()),
            ReservationDetailPaymentResponse.from(result.payment()),
            ReservationDetailReviewResponse.from(result.review())
        );
    }
}
