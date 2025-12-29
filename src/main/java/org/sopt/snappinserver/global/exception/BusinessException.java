package org.sopt.snappinserver.global.exception;

import lombok.Getter;
import org.sopt.snappinserver.global.response.code.ErrorCode;

@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode, String detail) {
        super(errorCode.getMessage() + " - " + detail);
        this.errorCode = errorCode;
    }

}
