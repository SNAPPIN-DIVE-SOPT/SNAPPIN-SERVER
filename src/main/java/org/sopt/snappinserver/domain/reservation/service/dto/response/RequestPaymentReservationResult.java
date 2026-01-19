package org.sopt.snappinserver.domain.reservation.service.dto.response;

import java.util.List;
import org.sopt.snappinserver.domain.reservation.domain.enums.ReservationStatus;
import org.sopt.snappinserver.domain.reservation.service.dto.request.ExtraPriceCommand;

public record RequestPaymentReservationResult(
    Long reservationId,
    ReservationStatus status,
    int basePrice,
    List<ExtraPriceCommand> extraPrices,
    int totalPrice
) {
}

