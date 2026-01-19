package org.sopt.snappinserver.domain.reservation.service.dto.request;

import org.sopt.snappinserver.api.v1.reservation.dto.request.ExtraPriceRequest;

public record ExtraPriceCommand(String name, int amount) {

    public static ExtraPriceCommand from(ExtraPriceRequest extraPrice) {
        return new ExtraPriceCommand(extraPrice.name(), extraPrice.amount());
    }
}
