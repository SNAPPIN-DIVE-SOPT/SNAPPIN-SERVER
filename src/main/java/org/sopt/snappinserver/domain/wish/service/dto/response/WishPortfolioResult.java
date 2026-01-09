package org.sopt.snappinserver.domain.wish.service.dto.response;

public record WishPortfolioResult(Long portfolioId, Boolean liked) {

    public static WishPortfolioResult of(Long portfolioId, Boolean liked) {
        return new WishPortfolioResult(portfolioId, liked);
    }
}
