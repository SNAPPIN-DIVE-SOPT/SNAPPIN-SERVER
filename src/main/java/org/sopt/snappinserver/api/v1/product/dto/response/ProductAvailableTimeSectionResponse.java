package org.sopt.snappinserver.api.v1.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "오전/오후 시간대 섹션 DTO")
public record ProductAvailableTimeSectionResponse(

    @Schema(description = "시간대 구분", example = "am")
    String label,

    @Schema(description = "해당 시간대 슬롯 목록")
    List<ProductAvailableTimeResponse> slots
) {}
