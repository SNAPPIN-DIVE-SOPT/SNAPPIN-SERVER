package org.sopt.snappinserver.api.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.sopt.snappinserver.domain.product.service.dto.response.ProductClosedDatesResult;

@Schema(description = "상품 달별 휴무일 조회 응답 DTO")
public record ProductClosedDatesResponse(

    @Schema(
        description = "해당 월의 휴무일 목록 (yyyy-MM-dd)",
        example = "[\"2026-03-03\", \"2026-03-10\", \"2026-03-17\"]"
    )
    List<String> closedDates

) {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    public static ProductClosedDatesResponse from(ProductClosedDatesResult result) {
        return new ProductClosedDatesResponse(
            result.closedDates().stream()
                .map(date -> date.format(DATE_FORMATTER))
                .toList()
        );
    }
}
