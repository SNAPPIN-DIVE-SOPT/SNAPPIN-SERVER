package org.sopt.snappinserver.domain.question.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.global.response.code.common.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum QuestionErrorCode implements ErrorCode {

    // 400 BAD REQUEST
    QUESTION_DOMAIN_REQUIRED(400, "QUESTION_400_001", "질문 관련 도메인은 필수입니다."),
    CONTENTS_REQUIRED(400, "QUESTION_400_002", "질문 내용은 필수입니다."),
    CONTENTS_TOO_LONG(400, "QUESTION_400_003", "질문 내용 길이는 200자 이하입니다."),
    STEP_REQUIRED(400, "QUESTION_400_004", "질문 단계는 필수입니다."),

    // 401 UNAUTHORIZED

    // 403 FORBIDDEN

    // 404 NOT FOUND

    ;

    private final int status;
    private final String code;
    private final String message;
}
