package org.sopt.snappinserver.api.photo.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.global.response.code.common.SuccessCode;

@Getter
@RequiredArgsConstructor
public enum PhotoSuccessCode implements SuccessCode {

    // 200 OK


    // 201 CREATED
    PHOTO_MOOD_CREATED(201, "PHOTO_201_001", "성공적으로 사진과 무드 태그를 연결했습니다."),

    ;

    private final int status;
    private final String code;
    private final String message;
}
