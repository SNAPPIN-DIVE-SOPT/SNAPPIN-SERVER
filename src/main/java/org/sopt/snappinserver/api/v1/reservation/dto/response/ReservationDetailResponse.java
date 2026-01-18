package org.sopt.snappinserver.api.v1.reservation.dto.response;

import org.sopt.snappinserver.domain.reservation.service.dto.response.GetReservationDetailResult;

public record ReservationDetailResponse(
    String status,
    ReservationDetailProductResponse productInfo,
    ReservationDetailInfoResponse reservationInfo,
    ReservationDetailPaymentResponse paymentInfo,
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
