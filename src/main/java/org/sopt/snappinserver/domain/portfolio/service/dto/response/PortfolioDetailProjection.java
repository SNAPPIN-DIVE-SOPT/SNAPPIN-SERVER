package org.sopt.snappinserver.domain.portfolio.service.dto.response;

import org.sopt.snappinserver.global.enums.SnapCategory;

public record PortfolioDetailProjection(
    Long portfolioId,
    String description,
    SnapCategory snapCategory,
    String startsAt,
    String placeName,
    Long photographerId,
    String photographerName,
    Long productId,
    String productName,
    int price
) {

}
