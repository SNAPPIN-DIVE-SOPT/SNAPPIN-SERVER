package org.sopt.snappinserver.api.category.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.global.response.code.common.SuccessCode;

@Getter
@RequiredArgsConstructor
public enum CategorySuccessCode implements SuccessCode {

    GET_CATEGORIES_OK(
            200,
            "CATEGORY_200_001",
            "촬영 상황 카테고리 조회에 성공했습니다."
    );

    private final int status;
    private final String code;
    private final String message;
}
