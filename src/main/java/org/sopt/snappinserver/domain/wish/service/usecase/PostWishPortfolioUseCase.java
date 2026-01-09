package org.sopt.snappinserver.domain.wish.service.usecase;

import org.sopt.snappinserver.domain.wish.service.dto.response.WishPortfolioResult;

public interface PostWishPortfolioUseCase {
    WishPortfolioResult execute(Long userId, Long portfolioId);
}
