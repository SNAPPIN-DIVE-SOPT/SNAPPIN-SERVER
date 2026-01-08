package org.sopt.snappinserver.api.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.sopt.snappinserver.domain.product.service.dto.response.ProductPeopleRangeResult;

@Getter
@AllArgsConstructor
@Schema(description = "촬영 가능 인원 수 조회 응답 DTO")
public class ProductPeopleRangeResponse {

    @Schema(description = "최소 인원", example = "1")
    private int minPeople;

    @Schema(description = "최대 인원", example = "5")
    private int maxPeople;

    public static ProductPeopleRangeResponse from(
        ProductPeopleRangeResult result
    ) {
        return new ProductPeopleRangeResponse(
            result.minPeople(),
            result.maxPeople()
        );
    }
}
