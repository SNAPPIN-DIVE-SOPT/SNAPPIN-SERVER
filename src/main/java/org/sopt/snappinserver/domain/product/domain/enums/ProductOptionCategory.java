package org.sopt.snappinserver.domain.product.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductOptionCategory {

    MIN_PEOPLE("최소 촬영 인원", "minPeople"),
    MAX_PEOPLE("최대 촬영 인원", "maxPeople"),
    PHOTOGRAPHER_COUNT("투입 작가 인원", "photographerCount"),
    DURATION_TIME("촬영 시간 (분 단위)", "durationTime"),
    PROVIDE_RAW("RAW 파일 제공 여부", "provideRaw"),
    PROVIDE_ORIGINAL_JPG("원본 JPG 제공 여부", "provideOriginalJpg"),
    ORIGINAL_JPG_COUNT("원본 JPG 제공 장수", "originalJpgCount"),
    ORIGINAL_DELIVERY_TIME("원본 제공 시점", "originalDeliveryTime"),
    PROVIDE_VIDEO("동영상 제공 여부", "provideVideo"),
    FREE_REVISION_COUNT("무료 수정 횟수", "freeRevisionCount"),
    FINAL_CUT_COUNT("최종 제공 장수", "finalCutCount"),
    FINAL_DELIVERY_TIME("최종 결과물 전달 소요 시간", "finalDeliveryTime"),
    CAN_ADD_PHOTO("장수 추가 가능 여부", "canAddPhoto"),
    ;

    private final String option;
    private final String fieldKey;
}
