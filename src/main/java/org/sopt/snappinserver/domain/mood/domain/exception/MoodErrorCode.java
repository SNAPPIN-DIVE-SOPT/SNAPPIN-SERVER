package org.sopt.snappinserver.domain.mood.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.global.response.code.common.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum MoodErrorCode implements ErrorCode {

    // 400 BAD REQUEST
    CATEGORY_REQUIRED(400, "MOOD_400_001", "무드 카테고리는 필수입니다."),
    NAME_REQUIRED(400, "MOOD_400_002", "무드 이름은 필수입니다."),
    NAME_TOO_LONG(400, "MOOD_400_003", "무드 이름 길이는 10자 이하입니다."),
    DEFINITION_REQUIRED(400, "MOOD_400_004", "무드 정의가 필요합니다."),
    DEFINITION_TOO_LONG(400, "MOOD_400_005", "무드 정의 길이는 1024자 이하입니다."),

    // 401 UNAUTHORIZED

    // 403 FORBIDDEN

    // 404 NOT FOUND

    ;

    private final int status;
    private final String code;
    private final String message;
}
