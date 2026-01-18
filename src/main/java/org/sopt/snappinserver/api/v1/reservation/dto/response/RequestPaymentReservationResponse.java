package org.sopt.snappinserver.api.v1.reservation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.snappinserver.domain.reservation.domain.enums.ReservationStatus;
import org.sopt.snappinserver.domain.reservation.service.dto.response.RequestPaymentReservationResult;

@Schema(description = "예약 결제 요청 응답 DTO")
public record RequestPaymentReservationResponse(

    @Schema(description = "결제 요청 처리된 예약 ID", example = "501")
    Long reservationId,

    @Schema(description = "변경된 예약 상태 ", example = "PAYMENT_REQUESTED")
    ReservationStatus status,

    @Schema(description = "결제 요청 정보")
    Payment payment
) {

    public static RequestPaymentReservationResponse from(RequestPaymentReservationResult result) {
        return new RequestPaymentReservationResponse(
            result.reservationId(),
            result.status(),
            new Payment(
                result.basePrice(),
                result.extraPrice(),
                result.totalPrice()
            )
        );
    }

    public record Payment(int basePrice, int extraPrice, int totalPrice) {

    }
}
