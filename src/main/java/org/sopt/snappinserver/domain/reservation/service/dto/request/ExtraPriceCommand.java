package org.sopt.snappinserver.domain.reservation.service.dto.request;

import org.sopt.snappinserver.api.reservation.dto.request.ExtraPriceResult;
import org.sopt.snappinserver.api.reservation.dto.request.RequestPaymentReservationRequest;

public record ExtraPriceCommand(String name, int amount) {

    public static ExtraPriceCommand from(ExtraPriceResult extraPrice)
    {
        return new ExtraPriceCommand(
            extraPrice.name(),
            extraPrice.amount()
        );
    }
}
