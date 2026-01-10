package org.sopt.snappinserver.api.product.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.global.response.code.common.SuccessCode;

@Getter
@RequiredArgsConstructor
public enum ProductSuccessCode implements SuccessCode {

    GET_PRODUCT_REVIEWS_OK(200, "PRODUCT_200_001", "상품 리뷰 목록 조회에 성공했습니다."),
    GET_PRODUCT_PEOPLE_RANGE_OK(200, "PRODUCT_200_002", "상품의 촬영 가능 인원 수 조회에 성공했습니다."),
    GET_PRODUCT_AVAILABLE_DATE_OK(200, "PRODUCT_200_003", "상품의 날짜별 예약 가능 여부 조회에 성공했습니다."),
    GET_PRODUCT_AVAILABLE_TIMES_OK(200, "PRODUCT_200_004", "상품의 시간대별 예약 가능 여부 조회에 성공했습니다."),
    POST_PRODUCT_RESERVATION_OK(200, "PRODUCT_200_005", "상품 예약 요청에 성공했습니다.");

    private final int status;
    private final String code;
    private final String message;
}
