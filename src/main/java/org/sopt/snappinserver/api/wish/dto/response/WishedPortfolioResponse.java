package org.sopt.snappinserver.api.wish.dto.response;

import org.sopt.snappinserver.domain.wish.service.dto.response.WishedPortfolioResult;

public record WishedPortfolioResponse(
    Long id,
    String imageUrl
) {
    public static WishedPortfolioResponse from(WishedPortfolioResult result) {
        return new WishedPortfolioResponse(
            result.id(),
            result.imageUrl()
        );
    }
}
