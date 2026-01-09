package org.sopt.snappinserver.domain.wish.service.usecase;

import org.sopt.snappinserver.domain.wish.service.dto.response.WishPortfolioResult;

public interface PostWishPortfolioUseCase {
    WishPortfolioResult togglePortfolioWish(Long userId, Long portfolioId);
}
