package org.sopt.snappinserver.domain.product.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.global.response.code.common.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum ProductErrorCode implements ErrorCode {

    // 400 BAD REQUEST
    PHOTOGRAPHER_REQUIRED(400, "PRODUCT_400_001", "작가는 필수입니다."),
    TITLE_REQUIRED(400, "PRODUCT_400_002", "제목은 필수입니다."),
    TITLE_TOO_LONG(400, "PRODUCT_400_003", "제목 길이는 100자 이하입니다."),
    PRICE_REQUIRED(400, "PRODUCT_400_004", "가격은 필수입니다."),
    PRICE_TOO_EXPENSIVE(400, "PRODUCT_400_005", "가격은 100만원 이하입니다."),
    PRICE_TOO_CHEAP(400, "PRODUCT_400_006", "가격은 10원 이상입니다."),
    SNAP_CATEGORY_REQUIRED(400, "PRODUCT_400_007", "촬영 종류는 필수입니다."),
    DESCRIPTION_TOO_LONG(400, "PRODUCT_400_008", "상품 소개 길이는 1024자 이하입니다."),
    EQUIPMENT_TOO_LONG(400, "PRODUCT_400_009", "사용 장비 설명 길이는 512자 이하입니다."),
    PROCESS_DESCRIPTION_TOO_LONG(400, "PRODUCT_400_010", "촬영 진행 순서 길이는 1024자 이하입니다."),
    CAUTION_TOO_LONG(400, "PRODUCT_400_011", "기타 주의 사항 길이는 1024자 이하입니다."),
    STARTS_AT_REQUIRED(400, "PRODUCT_400_012", "예약 운영 시작일은 필수입니다."),
    ENDS_AT_REQUIRED(400, "PRODUCT_400_013", "예약 운영 종료일은 필수입니다."),
    STARTS_AT_AFTER_ENDS_AT(400, "PRODUCT_400_014", "예약 운영 시작일이 예약 운영 종료일보다 앞서야 합니다."),
    PRODUCT_REQUIRED(400, "PRODUCT_400_015", ""),
    PRODUCT_OPTION_CATEGORY_REQUIRED(400, "PRODUCT_400_016", ""),
    ANSWER_REQUIRED(400, "PRODUCT_400_017", ""),
    ANSWER_TOO_LONG(400, "PRODUCT_400_018", ""),
    INVALID_CURSOR(400, "PRODUCT_400_019", "유효하지 않은 커서 값입니다."),
    // 401 UNAUTHORIZED

    // 403 FORBIDDEN

    // 404 NOT FOUND
    PRODUCT_NOT_FOUND(404, "PRODUCT_404_001", "존재하지 않는 상품입니다."),
    PRODUCT_PEOPLE_RANGE_NOT_FOUND(404, "PRODUCT_404_002", "촬영 가능 인원 정보가 존재하지 않습니다."
    ),

    // 500 SERVER ERROR
    INVALID_PRODUCT_OPTION_FORMAT(500, "PRODUCT_500_001", "상품 옵션 데이터 형식이 올바르지 않습니다."),
    INVALID_PRODUCT_PEOPLE_RANGE(500, "PRODUCT_500_002", "최소 인원이 최대 인원보다 클 수 없습니다.");

    private final int status;
    private final String code;
    private final String message;
}
