package org.sopt.snappinserver.domain.product.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductOptionCategory {

    MIN_PEOPLE("최소 촬영 인원"),
    MAX_PEOPLE("최대 촬영 인원"),
    PHOTOGRAPHER_COUNT("투입 작가 인원"),
    DURATION_TIME("촬영 시간 (분 단위)"),
    PROVIDE_RAW("RAW 파일 제공 여부"),
    PROVIDE_ORIGINAL_JPG("원본 JPG 제공 여부"),
    ORIGINAL_JPG_COUNT("원본 JPG 제공 장수"),
    ORIGINAL_DELIVERY_TIME("원본 제공 시점"),
    PROVIDE_VIDEO("동영상 제공 여부"),
    FREE_REVISION_COUNT("무료 수정 횟수"),
    FINAL_CUT_COUNT("최종 제공 장수"),
    FINAL_DELIVERY_TIME("최종 결과물 전달 소요 시간"),
    ;

    private final String option;
}
