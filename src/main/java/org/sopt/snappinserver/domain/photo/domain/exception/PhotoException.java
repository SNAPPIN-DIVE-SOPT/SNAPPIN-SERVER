package org.sopt.snappinserver.domain.photo.domain.exception;

import org.sopt.snappinserver.global.exception.BusinessException;

public class PhotoException extends BusinessException {

    public PhotoException(PhotoErrorCode photoErrorCode) {
        super(photoErrorCode);
    }
}
