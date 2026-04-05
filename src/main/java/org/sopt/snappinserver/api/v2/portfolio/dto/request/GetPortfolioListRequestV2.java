package org.sopt.snappinserver.api.v2.portfolio.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import java.util.List;
import org.sopt.snappinserver.domain.portfolio.domain.enums.PortfolioSortType;
import org.sopt.snappinserver.global.enums.SnapCategory;

@Schema(description = "포폴 목록 조회 요청 DTO - 쿼리 파라미터용")
public record GetPortfolioListRequestV2(

    @Schema(description = "무드 아이디 목록 (,로 구분, 개수 제한 없음)")
    List<Long> moodIds,

    @Schema(description = "상품 ID")
    @Positive(message = "상품 ID는 양수값이어야 합니다.")
    Long productId,

    @Schema(description = "스냅 작가 ID")
    @Positive(message = "스냅 작가 ID는 양수값이어야 합니다.")
    Long photographerId,

    @Schema(description = "촬영 상황 (스냅 유형)")
    SnapCategory snapCategory,

    @Schema(description = "장소 ID")
    @Positive(message = "장소 ID는 양수값이어야 합니다.")
    Long placeId,

    @Schema(description = "커서 값")
    Long cursor,

    @Schema(description = "정렬 기준 (RECOMMENDED, LATEST, POPULAR). 기본값: RECOMMENDED")
    PortfolioSortType sort,

    @Schema(description = "최소 가격")
    @Positive(message = "최소 가격은 양수값이어야 합니다.")
    Integer minPrice,

    @Schema(description = "최대 가격")
    @Positive(message = "최대 가격은 양수값이어야 합니다.")
    Integer maxPrice
) {

}
