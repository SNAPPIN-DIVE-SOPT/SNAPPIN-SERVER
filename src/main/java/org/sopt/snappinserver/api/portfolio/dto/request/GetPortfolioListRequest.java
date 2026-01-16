package org.sopt.snappinserver.api.portfolio.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import java.util.List;
import org.sopt.snappinserver.global.enums.SnapCategory;

@Schema(description = "포폴 목록 조회 요청 DTO - 쿼리 파라미터용")
public record GetPortfolioListRequest(

    @Schema(description = "무드 아이디 목록(,로 구분, 개수 제한 없음)")
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
    Long cursor
) {

}
