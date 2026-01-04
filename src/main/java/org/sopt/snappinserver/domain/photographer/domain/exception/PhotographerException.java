package org.sopt.snappinserver.domain.photographer.domain.exception;

import org.sopt.snappinserver.global.exception.BusinessException;

public class PhotographerException extends BusinessException {

    public PhotographerException(PhotographerErrorCode photographerErrorCode) {
        super(photographerErrorCode);
    }
}
