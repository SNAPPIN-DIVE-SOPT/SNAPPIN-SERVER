package org.sopt.snappinserver.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.global.response.code.common.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    // 400 BAD REQUEST
    INVALID_MAPPING_PARAMETER(400, "COMMON_400_001", "매핑할 수 없는 값입니다."),
    INVALID_REQUEST_VARIABLE(400, "COMMON_400_002", "엔드포인트에 잘못된 요청값이 있습니다."),

    // 401 UNAUTHENTICATED
    UNAUTHORIZED(401, "COMMON_401_001", "인증에 실패했습니다."),

    // 403 FORBIDDEN
    FORBIDDEN(403, "COMMON_403_001", "접근 권한이 없습니다."),

    // 404 NOT FOUND
    RESOURCE_NOT_FOUND(404, "COMMON_404_001", "존재하지 않는 리소스입니다."),

    // 500 INTERNAL SERVER ERROR
    INTERNAL_SERVER_ERROR(500, "COMMON_500_001", "서버 내부 오류가 발생했습니다."),

    ;

    private final int status;
    private final String code;
    private final String message;
}
