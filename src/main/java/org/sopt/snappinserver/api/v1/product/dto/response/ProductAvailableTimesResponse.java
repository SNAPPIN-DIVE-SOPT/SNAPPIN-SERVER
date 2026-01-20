package org.sopt.snappinserver.api.v1.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import org.sopt.snappinserver.domain.product.service.dto.response.ProductAvailableTimesResult;

@Schema(description = "상품 예약 가능 시간대 응답 DTO")
public record ProductAvailableTimesResponse(

    @Schema(description = "조회 기준 날짜", example = "2026-03-15")
    String date,

    @Schema(description = "오전/오후 시간대 목록")
    List<ProductAvailableTimeSectionResponse> sections
) {

    public static ProductAvailableTimesResponse from(
        ProductAvailableTimesResult result
    ) {
        Map<Boolean, List<ProductAvailableTimeResponse>> partitioned =
            result.times().stream()
                .map(ProductAvailableTimeResponse::from)
                .collect(
                    java.util.stream.Collectors.partitioningBy(
                        slot -> LocalTime.parse(slot.time()).isBefore(LocalTime.NOON)
                    )
                );

        return new ProductAvailableTimesResponse(
            result.date().toString(),
            List.of(
                new ProductAvailableTimeSectionResponse(
                    "am",
                    partitioned.getOrDefault(true, List.of())
                ),
                new ProductAvailableTimeSectionResponse(
                    "pm",
                    partitioned.getOrDefault(false, List.of())
                )
            )
        );
    }
}
