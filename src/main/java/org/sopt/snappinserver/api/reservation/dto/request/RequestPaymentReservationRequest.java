package org.sopt.snappinserver.api.reservation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

public record RequestPaymentReservationRequest(

    @NotNull @Positive Integer basePrice,

    @Valid List<ExtraPriceResult> extraPrices,

    @NotNull @Positive Integer totalPrice
) {
}
