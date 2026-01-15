package org.sopt.snappinserver.domain.reservation.service.dto.request;

import java.util.List;
import org.sopt.snappinserver.api.reservation.dto.request.RequestPaymentReservationRequest;

public record RequestPaymentReservationCommand(
    Long photographerUserId,
    Long reservationId,
    int basePrice,
    List<ExtraPriceCommand> extraPrices,
    int totalPrice
) {

    public static RequestPaymentReservationCommand from(
        Long photographerUserId,
        Long reservationId,
        RequestPaymentReservationRequest request
    ) {
        return new RequestPaymentReservationCommand(
            photographerUserId,
            reservationId,
            request.basePrice(),
            request.extraPrices() == null
                ? List.of()
                : request.extraPrices().stream().map(ExtraPriceCommand::from).toList(),
            request.totalPrice()
        );
    }
}
