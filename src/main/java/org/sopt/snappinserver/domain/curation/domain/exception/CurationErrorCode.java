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
    STEP_REQUIRED(400, "CURATION_400_004", "무드 큐레이션 단계는 필수입니다."),
    INVALID_STEP_RANGE(400, "CURATION_400_005", "무드 큐레이션 단계는 1 이상 5 이하입니다."),

    // 401 UNAUTHORIZED
    CURATION_LOGIN_REQUIRED(401, "CURATION_401_001", "무드 큐레이션은 로그인이 필요합니다."),

    // 403 FORBIDDEN

    // 404 NOT FOUND
    QUESTION_NOT_FOUND(404, "CURATION_404_001", "해당 무드 큐레이션 질문을 찾을 수 없습니다."),
    PHOTO_NOT_FOUND(404, "CURATION_404_002", "해당 이미지를 찾을 수 없습니다."),
    USER_NOT_FOUND(404, "CURATION_404_003", "유저 정보를 찾을 수 없습니다."),
    PHOTO_ID_NOT_FOUND(404, "CURATION_404_004", "존재하지 않는 사진 ID가 포함되어 있습니다."),

    ;

    private final int status;
    private final String code;
    private final String message;
}
