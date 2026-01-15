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
    REVIEW_ALREADY_EXISTS(400, "REVIEW_400_007", "이미 존재하는 리뷰입니다."),
    UNSUPPORTED_IMAGE_TYPE(400, "REVIEW_400_008", "지원하지 않는 파일 형식입니다."),
    INVALID_FILE_NAME(400, "REVIEW_400_009", "지원하지 않는 파일 확장자입니다."),

    // 401 UNAUTHORIZED

    // 403 FORBIDDEN
    USER_MUST_LOGIN_BY_CLIENT(403, "REVIEW_403_001", "유저는 반드시 고객으로 로그인해야 합니다."),

    // 404 NOT FOUND
    USER_NOT_FOUND(404, "REVIEW_404_001", "해당 유저를 찾을 수 없습니다."),
    REVIEW_NOT_FOUND(404, "REVIEW_404_002", "해당 리뷰를 찾을 수 없습니다."),

    ;

    private final int status;
    private final String code;
    private final String message;
}
