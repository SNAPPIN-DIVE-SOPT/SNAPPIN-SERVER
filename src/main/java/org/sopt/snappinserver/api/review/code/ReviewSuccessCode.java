package org.sopt.snappinserver.api.review.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.global.response.code.common.SuccessCode;

@Getter
@RequiredArgsConstructor
public enum ReviewSuccessCode implements SuccessCode {

    // 200 OK
    POST_PRESIGNED_URL_OK(200, "REVIEW_200_001", "성공적으로 리뷰 이미지 Presigned URL을 발급했습니다."),
    GET_REVIEW_DETAIL_OK(200, "REVIEW_200_002", "리뷰 상세 조회에 성공했습니다.")

    // 201 CREATED

    ;

    private final int status;
    private final String code;
    private final String message;
}
