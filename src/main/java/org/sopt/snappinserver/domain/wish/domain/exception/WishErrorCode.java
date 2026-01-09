package org.sopt.snappinserver.domain.wish.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.global.response.code.common.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum WishErrorCode implements ErrorCode {

    // 400 BAD REQUEST
    USER_REQUIRED(400, "WISH_400_001", "좋아요를 누른 사용자는 필수입니다."),
    PRODUCT_REQUIRED(400, "WISH_400_002", "좋아요한 상품은 필수입니다."),
    PORTFOLIO_REQUIRED(400, "WISH_400_003", "좋아요한 포트폴리오는 필수입니다."),

    // 401 UNAUTHORIZED

    // 403 FORBIDDEN

    // 404 NOT FOUND
    USER_NOT_FOUND(404, "WISH_404_001", "존재하지 않는 사용자입니다."),
    PRODUCT_NOT_FOUND(404, "WISH_404_002", "존재하지 않는 상품입니다."),
    PORTFOLIO_NOT_FOUND(404, "WISH_404_003", "존재하지 않는 포트폴리오입니다.");

    private final int status;
    private final String code;
    private final String message;
}
