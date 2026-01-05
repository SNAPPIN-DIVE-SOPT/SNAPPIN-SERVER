package org.sopt.snappinserver.domain.wish.domain.exception;

import org.sopt.snappinserver.global.exception.BusinessException;

public class WishException extends BusinessException {

    public WishException(WishErrorCode wishErrorCode) {
        super(wishErrorCode);
    }
}
