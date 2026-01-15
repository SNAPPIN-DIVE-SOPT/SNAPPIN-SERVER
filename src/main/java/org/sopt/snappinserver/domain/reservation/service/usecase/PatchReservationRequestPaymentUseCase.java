package org.sopt.snappinserver.domain.reservation.service.usecase;

import org.sopt.snappinserver.domain.reservation.service.dto.request.RequestPaymentReservationCommand;
import org.sopt.snappinserver.domain.reservation.service.dto.response.RequestPaymentReservationResult;

public interface PatchReservationRequestPaymentUseCase {

    RequestPaymentReservationResult requestPaymentReservation(
        RequestPaymentReservationCommand command
    );
}
