package org.sopt.snappinserver.domain.reservation.domain.exception;

import org.sopt.snappinserver.global.exception.BusinessException;

public class ReservationException extends BusinessException {

    public ReservationException(ReservationErrorCode reservationErrorCode) {
        super(reservationErrorCode);
    }
}
