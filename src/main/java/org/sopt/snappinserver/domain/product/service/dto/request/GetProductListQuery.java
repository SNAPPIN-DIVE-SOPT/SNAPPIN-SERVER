package org.sopt.snappinserver.domain.product.service.dto.request;

import java.time.LocalDate;
import java.util.List;
import org.sopt.snappinserver.global.enums.SnapCategory;

public record GetProductListQuery(
    List<Long> moodIds,
    Long photographerId,
    SnapCategory snapCategory,
    Long placeId,
    LocalDate date,
    Integer peopleCount,
    Long cursor
) {

}
