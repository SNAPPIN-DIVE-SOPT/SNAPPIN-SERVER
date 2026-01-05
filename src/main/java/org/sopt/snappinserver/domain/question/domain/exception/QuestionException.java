package org.sopt.snappinserver.domain.question.domain.exception;

import org.sopt.snappinserver.global.exception.BusinessException;

public class QuestionException extends BusinessException {

    public QuestionException(QuestionErrorCode questionErrorCode) {
        super(questionErrorCode);
    }
}
