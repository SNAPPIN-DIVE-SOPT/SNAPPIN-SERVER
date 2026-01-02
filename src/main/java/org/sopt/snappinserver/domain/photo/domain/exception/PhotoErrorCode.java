package org.sopt.snappinserver.domain.photo.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.global.response.code.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum PhotoErrorCode implements ErrorCode {

    // 400 BAD REQUEST
    IMAGE_URL_REQUIRED(400, "PHOTO_400_001", "이미지 경로는 필수입니다."),
    IMAGE_URL_TOO_LONG(400, "PHOTO_400_002", "이미지 경로 길이는 1024자 이하입니다."),
    EMBEDDING_REQUIRED(400, "PHOTO_400_003", "임베딩 벡터는 필수입니다."),
    EMBEDDING_DIMENSION_WRONG(400, "PHOTO_400_004", "임베딩 차원은 512입니다."),

    // 401 UNAUTHORIZED

    // 403 FORBIDDEN

    // 404 NOT FOUND
    ;

    private final int status;
    private final String code;
    private final String message;
}
