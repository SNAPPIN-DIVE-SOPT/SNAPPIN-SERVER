package org.sopt.snappinserver.domain.user.domain.exception;

import org.sopt.snappinserver.global.exception.BusinessException;

public class UserException extends BusinessException {

    public UserException(UserErrorCode userErrorCode) {
        super(userErrorCode);
    }
}
