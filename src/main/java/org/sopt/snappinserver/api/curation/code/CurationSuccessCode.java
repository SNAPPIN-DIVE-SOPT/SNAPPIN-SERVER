package org.sopt.snappinserver.api.curation.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.global.response.code.common.SuccessCode;

@Getter
@RequiredArgsConstructor
public enum CurationSuccessCode implements SuccessCode {

    // 200 OK
    GET_CURATION_QUESTION_SUCCESS(200, "CURATION_200_001", "무드 큐레이션 질문을 성공적으로 조회했습니다."),

    // 201 CREATED

    ;

    private final int status;
    private final String code;
    private final String message;
}
