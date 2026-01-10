package org.sopt.snappinserver.global.s3;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.global.response.code.common.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum S3ErrorCode implements ErrorCode {

    // 400 BAD REQUEST
    S3_KEY_REQUIRED(400, "S3_400_001", "S3 키는 필수입니다."),

    // 503 Service Unavailable
    S3_SERVICE_UNAVAILABLE(503, "S3_503_001", "Presigned URL 발급에 실패했습니다."),
    ;

    private final int status;
    private final String code;
    private final String message;
}
