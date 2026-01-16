package org.sopt.snappinserver.domain.portfolio.service.dto.request;

import java.util.List;
import org.sopt.snappinserver.global.enums.SnapCategory;

public record GetPortfolioListQuery(
    List<Long> moodIds,
    Long productId,
    Long photographerId,
    SnapCategory snapCategory,
    Long placeId,
    Long cursor
) {

}
