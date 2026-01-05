package org.sopt.snappinserver.domain.curation.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.global.response.code.common.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum CurationErrorCode implements ErrorCode {

    // 400 BAD REQUEST
    USER_REQUIRED(400, "CURATION_400_001", "큐레이션을 진행한 유저는 필수입니다."),
    MOOD_REQUIRED(400, "CURATION_400_002", "유저 맞춤 무드는 필수입니다."),
    RANK_REQUIRED(400, "CURATION_400_003", "무드 순위는 필수입니다."),

    // 401 UNAUTHORIZED

    // 403 FORBIDDEN

    // 404 NOT FOUND

    ;

    private final int status;
    private final String code;
    private final String message;
}
