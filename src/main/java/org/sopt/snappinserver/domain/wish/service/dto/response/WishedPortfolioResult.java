package org.sopt.snappinserver.domain.wish.service.dto.response;

public record WishedPortfolioResult(Long id, String imageUrl) {

    public static WishedPortfolioResult of(Long id, String imageUrl) {
        return new WishedPortfolioResult(id, imageUrl);
    }
}
