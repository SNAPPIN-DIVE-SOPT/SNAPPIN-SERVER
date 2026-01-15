package org.sopt.snappinserver.api.reservation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

public record RequestPaymentReservationRequest(

    @NotNull @Positive Integer basePrice,

    @Valid List<ExtraPrice> extraPrices,

    @NotNull @Positive Integer totalPrice
) {

    public record ExtraPrice(
        @NotNull String name,
        @NotNull @Positive Integer amount
    ) {

    }
}
