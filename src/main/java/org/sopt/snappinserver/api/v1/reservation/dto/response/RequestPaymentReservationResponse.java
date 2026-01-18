package org.sopt.snappinserver.api.v1.reservation.dto.response;

import org.sopt.snappinserver.domain.reservation.domain.enums.ReservationStatus;
import org.sopt.snappinserver.domain.reservation.service.dto.response.RequestPaymentReservationResult;

public record RequestPaymentReservationResponse(
    Long reservationId,
    ReservationStatus status,
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
