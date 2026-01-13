package org.sopt.snappinserver.api.reservation.dto.response;

public record ReservationDetailPaymentResponse(
    int basePrice,
    int extraPrice,
    int totalPrice
) {
    public static ReservationDetailPaymentResponse from(
        org.sopt.snappinserver.domain.reservation.service.dto.response.GetReservationDetailPaymentResult result
    ) {
        if (result == null) return null;

        return new ReservationDetailPaymentResponse(
            result.basePrice(),
            result.extraPrice(),
            result.totalPrice()
        );
    }
}
