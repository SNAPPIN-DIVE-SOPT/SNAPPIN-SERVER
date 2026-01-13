package org.sopt.snappinserver.api.portfolio.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.global.response.code.common.SuccessCode;

@Getter
@RequiredArgsConstructor
public enum PortfolioSuccessCode implements SuccessCode {

    // 200 OK
    GET_POPULAR_PORTFOLIOS_OK(200, "PORTFOLIO_200_001", "성공적으로 인기 무드 기반 포트폴리오 목록을 조회했습니다."),

    // 201 CREATED

    ;

    private final int status;
    private final String code;
    private final String message;
}
