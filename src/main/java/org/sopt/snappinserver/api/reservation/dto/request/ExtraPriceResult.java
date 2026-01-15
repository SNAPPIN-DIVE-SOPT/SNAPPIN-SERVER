package org.sopt.snappinserver.api.reservation.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ExtraPriceResult(
    @NotNull String name,
    @NotNull @Positive Integer amount
) {

}
