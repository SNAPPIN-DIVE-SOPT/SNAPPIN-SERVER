package org.sopt.snappinserver.domain.mood.domain.exception;

import org.sopt.snappinserver.global.exception.BusinessException;

public class MoodException extends BusinessException {

    public MoodException(MoodErrorCode moodErrorCode) {
        super(moodErrorCode);
    }
}
