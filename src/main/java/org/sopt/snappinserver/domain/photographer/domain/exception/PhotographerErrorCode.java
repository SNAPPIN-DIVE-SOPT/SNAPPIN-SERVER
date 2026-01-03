package org.sopt.snappinserver.domain.photographer.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.global.response.code.common.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum PhotographerErrorCode implements ErrorCode {

    // 400 BAD REQUEST
    USER_REQUIRED(400, "PHOTOGRAPHER_400_001", "유저는 필수입니다."),
    NAME_REQUIRED(400, "PHOTOGRAPHER_400_002", "이름은 필수입니다."),
    NAME_LENGTH_TOO_LONG(400, "PHOTOGRAPHER_400_003", "이름 길이는 10자 이하입니다."),
    NICKNAME_REQUIRED(400, "PHOTOGRAPHER_400_004", "작가 활동명은 필수입니다."),
    NICKNAME_LENGTH_TOO_LONG(400, "PHOTOGRAPHER_400_005", "작가 활동명 길이는 20자 이하입니다."),
    GENDER_REQUIRED(400, "PHOTOGRAPHER_400_006", "성별은 필수입니다."),
    SPECIALTY_REQUIRED(400, "PHOTOGRAPHER_400_007", "전문 분야는 필수입니다."),
    PHOTOGRAPHER_REQUIRED(400, "PHOTOGRAPHER_400_008", "스냅 작가는 필수입니다."),
    WEEK_DAY_REQUIRED(400, "PHOTOGRAPHER_400_009", "요일은 필수입니다."),
    START_TIME_REQUIRED(400, "PHOTOGRAPHER_400_010", "시작 시각은 필수입니다."),
    END_TIME_REQUIRED(400, "PHOTOGRAPHER_400_011", "종료 시각은 필수입니다."),
    START_TIME_AFTER_THAN_END_TIME(400, "PHOTOGRAPHER_400_012", "스케쥴 시작 시간이 종료 시간보다 앞서야 합니다."),

    // 401 UNAUTHORIZED

    // 403 FORBIDDEN

    // 404 NOT FOUND

    ;

    private final int status;
    private final String code;
    private final String message;
}
