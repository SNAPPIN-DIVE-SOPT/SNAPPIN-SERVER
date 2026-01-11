package org.sopt.snappinserver.api.mood.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.global.response.code.common.SuccessCode;

@Getter
@RequiredArgsConstructor
public enum MoodSuccessCode implements SuccessCode {

    // 200 OK
    GET_ALL_MOOD_TAGS_OK(200, "MOOD_200_001", "성공적으로 전체 무드 필터값을 조회했습니다."),

    // 201 CREATED

    ;

    private final int status;
    private final String code;
    private final String message;
}
