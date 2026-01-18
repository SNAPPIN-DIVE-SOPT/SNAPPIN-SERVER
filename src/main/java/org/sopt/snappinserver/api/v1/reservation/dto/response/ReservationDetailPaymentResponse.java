package org.sopt.snappinserver.api.v1.reservation.dto.response;

import org.sopt.snappinserver.domain.reservation.service.dto.response.GetReservationDetailPaymentResult;

public record ReservationDetailPaymentResponse(
    int basePrice,
    int extraPrice,
    int totalPrice
) {

    public static ReservationDetailPaymentResponse from(GetReservationDetailPaymentResult result) {
        if (result == null) {
            return null;
        }

        return new ReservationDetailPaymentResponse(
            result.basePrice(),
            result.extraPrice(),
            result.totalPrice()
        );
    }
}
