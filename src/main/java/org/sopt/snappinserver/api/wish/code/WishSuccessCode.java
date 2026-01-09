package org.sopt.snappinserver.api.wish.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.global.response.code.common.SuccessCode;

@Getter
@RequiredArgsConstructor
public enum WishSuccessCode implements SuccessCode {

    POST_WISH_LIKE_PORTFOLIO_OK(200, "WISH_200_001", "포트폴리오 좋아요 처리에 성공했습니다."),
    POST_WISH_CANCEL_PORTFOLIO_OK(200, "WISH_200_002", "포트폴리오 좋아요 취소에 성공했습니다.");


    private final int status;
    private final String code;
    private final String message;
}