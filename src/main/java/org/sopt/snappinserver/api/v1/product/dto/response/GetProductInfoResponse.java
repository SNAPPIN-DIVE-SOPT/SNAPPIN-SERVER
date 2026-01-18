package org.sopt.snappinserver.api.v1.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Optional;
import org.sopt.snappinserver.domain.product.service.dto.response.GetProductInfoResult;

@Schema(description = "상품 안내 정보 응답 DTO")
public record GetProductInfoResponse(

    @Schema(description = "촬영 종류 (유형)")
    String snapCategory,

    @Schema(description = "촬영 장소 (지역)")
    List<String> regions,

    @Schema(description = "스냅 무드")
    List<String> moods,

    @Schema(description = "최대 촬영 인원")
    String maxPeople,

    @Schema(description = "촬영 작가 인원")
    String photographerCount,

    @Schema(description = "촬영 시간 (시간단위)")
    String durationTime,

    @Schema(description = "RAW 파일 제공 여부")
    String provideRaw,

    @Schema(description = "원본 JPG 제공 여부")
    String provideOriginalJpg,

    @Schema(description = "원본 JPG 제공 장수")
    String originalJpgCount,

    @Schema(description = "원본 제공 시점")
    String originalDeliveryTime,

    @Schema(description = "동영상 제공 여부")
    String provideVideo,

    @Schema(description = "무료 수정 횟수")
    String freeRevisionCount,

    @Schema(description = "최종 결과물 제공 장수")
    String finalCutCount,

    @Schema(description = "최종 결과물 전달 소요시간")
    String finalDeliveryTime,

    @Schema(description = "상품 소개")
    String description,

    @Schema(description = "촬영 진행 순서 ")
    String processDescription,

    @Schema(description = "사용 장비")
    String equipment,

    @Schema(description = "기타 주의 사항")
    String caution
) {

    public static GetProductInfoResponse from(
        GetProductInfoResult getProductInfoResult
    ) {
        return new GetProductInfoResponse(
            getProductInfoResult.snapCategory(),
            getProductInfoResult.regions(),
            getProductInfoResult.moods(),
            Optional.ofNullable(getProductInfoResult.maxPeople())
                .map(m -> m.concat("명"))
                .orElse(null),
            getProductInfoResult.photographerCount(),
            Optional.ofNullable(getProductInfoResult.durationTime())
                .map(d -> d.concat("시간"))
                .orElse(null),
            getProductInfoResult.provideRaw(),
            getProductInfoResult.provideOriginalJpg(),
            getProductInfoResult.originalJpgCount(),
            getProductInfoResult.originalDeliveryTime(),
            getProductInfoResult.provideVideo(),
            getProductInfoResult.freeRevisionCount(),
            getProductInfoResult.finalCutCount(),
            getProductInfoResult.finalDeliveryTime(),
            getProductInfoResult.description(),
            getProductInfoResult.processDescription(),
            getProductInfoResult.equipment(),
            getProductInfoResult.caution()
        );
    }
}
