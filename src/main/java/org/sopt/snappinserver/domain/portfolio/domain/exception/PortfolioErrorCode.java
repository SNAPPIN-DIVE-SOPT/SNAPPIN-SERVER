package org.sopt.snappinserver.domain.portfolio.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.global.response.code.common.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum PortfolioErrorCode implements ErrorCode {

    // 400 BAD REQUEST
    PRODUCT_REQUIRED(400, "PORTFOLIO_400_001", "상품은 필수입니다."),
    SNAP_CATEGORY_REQUIRED(400, "PORTFOLIO_400_002", "촬영 상황은 필수입니다."),
    STARTS_AT_REQUIRED(400, "PORTFOLIO_400_003", "시작 시각은 필수입니다."),
    ENDS_AT_REQUIRED(400, "PORTFOLIO_400_004", "종료 시각은 필수입니다."),
    STARTS_AT_AFTER_ENDS_AT(400, "PORTFOLIO_400_005", "시작 시각이 종료 시각보다 앞서야 합니다."),

    // 401 UNAUTHORIZED

    // 403 FORBIDDEN

    // 404 NOT FOUND
    PORTFOLIO_NOT_FOUND(404, "PORTFOLIO_404_001", "해당 포트폴리오를 찾을 수 없습니다."),
    PLACE_NOT_FOUND(404, "PORTFOLIO_404_002", "해당 장소를 찾을 수 없습니다."),
    PRODUCT_PHOTO_NOT_FOUND(404, "PORTFOLIO_404_003", "해당 상품 이미지를 찾을 수 없습니다."),
    PRODUCT_NOT_FOUND(404, "PORTFOLIO_404_004", "해당 상품을 찾을 수 없습니다."),
    USER_NOT_FOUND(404, "PORTFOLIO_404_005", "해당 유저를 찾을 수 없습니다."),

    ;

    private final int status;
    private final String code;
    private final String message;
}
