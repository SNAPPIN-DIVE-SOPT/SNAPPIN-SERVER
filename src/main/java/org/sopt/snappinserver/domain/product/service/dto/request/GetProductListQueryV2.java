package org.sopt.snappinserver.domain.product.service.dto.request;

import java.time.LocalDate;
import java.util.List;
import org.sopt.snappinserver.global.enums.SnapCategory;
import org.sopt.snappinserver.global.enums.SortType;

public record GetProductListQueryV2(
    List<Long> moodIds,
    Long photographerId,
    SnapCategory snapCategory,
    Long placeId,
    LocalDate date,
    Integer peopleCount,
    Long cursorId,
    Long cursorLikeCount,
    Double cursorAvgRating,
    SortType sort,
    Integer minPrice,
    Integer maxPrice,
    Long userId
) {

}
