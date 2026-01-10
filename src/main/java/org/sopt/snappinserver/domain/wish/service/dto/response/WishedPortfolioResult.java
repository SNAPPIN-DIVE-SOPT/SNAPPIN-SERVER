package org.sopt.snappinserver.domain.wish.service.dto.response;

import org.sopt.snappinserver.domain.portfolio.domain.entity.Portfolio;

public record WishedPortfolioResult(
    Long id,
    String imageUrl
) {
    public static WishedPortfolioResult from(Portfolio portfolio) {
        return new WishedPortfolioResult(
            portfolio.getId(),
            portfolio.getThumbnailImageUrl()
        );
    }
}
