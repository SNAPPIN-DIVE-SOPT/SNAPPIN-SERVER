package org.sopt.snappinserver.api.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.snappinserver.domain.product.service.dto.response.ProductPeopleRangeResult;

@Schema(description = "촬영 가능 인원 수 조회 응답 DTO")
public record ProductPeopleRangeResponse(

    @Schema(description = "최소 인원", example = "1")
    int minPeople,

    @Schema(description = "최대 인원", example = "5")
    int maxPeople

) {
    public static ProductPeopleRangeResponse from(
        ProductPeopleRangeResult result
    ) {
        return new ProductPeopleRangeResponse(
            result.minPeople(),
            result.maxPeople()
        );
    }
}
