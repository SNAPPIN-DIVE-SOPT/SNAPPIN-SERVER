package org.sopt.snappinserver.domain.reservation.service.dto.response;

import java.util.List;
import org.sopt.snappinserver.domain.reservation.domain.enums.ReservationStatus;

public record RequestPaymentReservationResult(
    Long reservationId,
    ReservationStatus status,
    int basePrice,
    List<ExtraPriceResult> extraPrices,
    int totalPrice
) {
}

