package org.sopt.snappinserver.domain.portfolio.service.dto.request;

import java.util.List;
import org.sopt.snappinserver.global.enums.SortType;
import org.sopt.snappinserver.global.enums.SnapCategory;

public record GetPortfolioListQueryV2(
    List<Long> moodIds,
    Long productId,
    Long photographerId,
    SnapCategory snapCategory,
    Long placeId,
    Long cursorId,
    Long cursorLikeCount,
    Double cursorAvgRating,
    SortType sort,
    Integer minPrice,
    Integer maxPrice,
    Long userId
) {

}
