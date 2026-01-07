package org.sopt.snappinserver.api.product.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.global.response.code.common.SuccessCode;

@Getter
@RequiredArgsConstructor
public enum ProductSuccessCode implements SuccessCode {

    GET_PRODUCT_REVIEWS_OK(200, "PRODUCT_200_001", "상품 리뷰 목록 조회에 성공했습니다.");

    private final int status;
    private final String code;
    private final String message;
}
