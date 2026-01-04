package org.sopt.snappinserver.domain.place.domain.exception;

import org.sopt.snappinserver.global.exception.BusinessException;

public class PlaceException extends BusinessException {

    public PlaceException(PlaceErrorCode placeErrorCode) {
        super(placeErrorCode);
    }
}
