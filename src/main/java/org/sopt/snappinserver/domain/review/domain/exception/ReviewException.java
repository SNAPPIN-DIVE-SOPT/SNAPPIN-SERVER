package org.sopt.snappinserver.domain.review.domain.exception;

import org.sopt.snappinserver.global.exception.BusinessException;

public class ReviewException extends BusinessException {

    public ReviewException(ReviewErrorCode reviewErrorCode) {
        super(reviewErrorCode);
    }
}
