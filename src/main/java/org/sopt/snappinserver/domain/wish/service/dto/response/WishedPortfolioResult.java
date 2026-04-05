package org.sopt.snappinserver.domain.wish.service.dto.response;

public record WishedPortfolioResult(Long id, String imageUrl, Integer likeCount) {

    public static WishedPortfolioResult of(Long id, String imageUrl, Integer likeCount) {
        return new WishedPortfolioResult(id, imageUrl, likeCount);
    }
}
