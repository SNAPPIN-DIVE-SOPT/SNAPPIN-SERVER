package org.sopt.snappinserver.domain.reservation.service.dto.response;

import java.util.List;

public record GetReservationDetailPaymentResult(
    int basePrice,
    List<ExtraPriceResult> extraPrices,
    int totalPrice
) {

}
