package org.sopt.snappinserver.domain.review.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.global.response.code.common.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum ReviewErrorCode implements ErrorCode {

    // 400 BAD REQUEST
    RESERVATION_REQUIRED(400, "REVIEW_400_001", "리뷰 대상 예약은 필수입니다."),
    RATING_REQUIRED(400, "REVIEW_400_002", "별점은 필수입니다."),
    RATING_SCORE_TOO_SMALL(400, "REVIEW_400_003", "별점은 1점 이상입니다."),
    RATING_SCORE_TOO_BIG(400, "REVIEW_400_004", "별점은 5점 이하입니다."),
    CONTENT_REQUIRED(400, "REVIEW_400_005", "본문은 필수입니다."),
    CONTENT_TOO_LONG(400, "REVIEW_400_006", "본문 길이는 512자 이하입니다."),

    // 401 UNAUTHORIZED

    // 403 FORBIDDEN

    // 404 NOT FOUND

    ;

    private final int status;
    private final String code;
    private final String message;
}
