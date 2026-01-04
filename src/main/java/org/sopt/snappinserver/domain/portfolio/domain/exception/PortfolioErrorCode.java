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

    ;

    private final int status;
    private final String code;
    private final String message;
}
