package org.sopt.snappinserver.domain.reservation.service.dto.response;

public record GetReservationDetailPaymentResult(
    int basePrice,
    int extraPrice,
    int totalPrice
) {

}
