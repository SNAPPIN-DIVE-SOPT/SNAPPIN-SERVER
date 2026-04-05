package org.sopt.snappinserver.global.response.code.product;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.global.response.code.common.SuccessCode;

@Getter
@RequiredArgsConstructor
public enum ProductSuccessCode implements SuccessCode {

    // 200 OK
    GET_PRODUCT_REVIEWS_OK(200, "PRODUCT_200_001", "상품 리뷰 목록 조회에 성공했습니다."),
    GET_PRODUCT_PEOPLE_RANGE_OK(200, "PRODUCT_200_002", "상품의 촬영 가능 인원 수 조회에 성공했습니다."),
    GET_PRODUCT_AVAILABLE_DATE_OK(200, "PRODUCT_200_003", "상품의 날짜별 예약 가능 여부 조회에 성공했습니다."),
    GET_PRODUCT_AVAILABLE_TIMES_OK(200, "PRODUCT_200_004", "상품의 시간대별 예약 가능 여부 조회에 성공했습니다."),
    GET_PRODUCT_DETAIL_OK(200, "PRODUCT_200_005", "상품 상세 조회에 성공했습니다."),
    GET_PRODUCT_PRICE_OK(200, "PRODUCT_200_006", "상품 기본 촬영 비용 조회에 성공했습니다."),
    GET_PRODUCT_LIST_OK(200, "PRODUCT_200_007", "상품 목록 조회에 성공했습니다."),
    GET_PRODUCT_DURATION_TIME_OK(200, "PRODUCT_200_008", "상품의 촬영 시간 조회에 성공했습니다."),
    GET_POPULAR_MOOD_PRODUCTS_OK(200, "PRODUCT_200_009", "인기 무드 상품 목록 조회에 성공했습니다."),
    GET_PRODUCT_EXTRA_INFO_OK(200, "PRODUCT_200_010", "상품 예약 부가 안내 조회에 성공했습니다."),

    // 201 CREATED
    POST_PRODUCT_RESERVATION_OK(201, "PRODUCT_201_001", "상품 예약 생성에 성공했습니다.");

    private final int status;
    private final String code;
    private final String message;
}
