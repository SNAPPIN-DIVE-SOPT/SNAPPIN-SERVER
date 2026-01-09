package org.sopt.snappinserver.domain.wish.service;

import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.portfolio.domain.entity.Portfolio;
import org.sopt.snappinserver.domain.portfolio.repository.PortfolioRepository;
import org.sopt.snappinserver.domain.user.domain.entity.User;
import org.sopt.snappinserver.domain.user.repository.UserRepository;
import org.sopt.snappinserver.domain.wish.domain.entity.WishPortfolio;
import org.sopt.snappinserver.domain.wish.domain.exception.WishErrorCode;
import org.sopt.snappinserver.domain.wish.domain.exception.WishException;
import org.sopt.snappinserver.domain.wish.repository.WishPortfolioRepository;
import org.sopt.snappinserver.domain.wish.service.dto.response.WishPortfolioResult;
import org.sopt.snappinserver.domain.wish.service.usecase.PostWishPortfolioUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostWishPortfolioService implements PostWishPortfolioUseCase {

    private final WishPortfolioRepository wishPortfolioRepository;
    private final UserRepository userRepository;
    private final PortfolioRepository portfolioRepository;

    @Override
    @Transactional
    public WishPortfolioResult togglePortfolioWish(Long userId, Long portfolioId) {
        User user = getUser(userId);
        Portfolio portfolio = getPortfolio(portfolioId);

        return wishPortfolioRepository.findByUserAndPortfolio(user, portfolio)
            .map(existingWish -> cancelWish(existingWish, portfolioId))
            .orElseGet(() -> likeWish(user, portfolio, portfolioId));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new WishException(WishErrorCode.USER_NOT_FOUND));
    }

    private Portfolio getPortfolio(Long portfolioId) {
        return portfolioRepository.findById(portfolioId)
            .orElseThrow(() -> new WishException(WishErrorCode.PORTFOLIO_NOT_FOUND));
    }

    private WishPortfolioResult cancelWish(WishPortfolio existingWish, Long portfolioId) {
        wishPortfolioRepository.delete(existingWish);
        return WishPortfolioResult.of(portfolioId, false);
    }

    private WishPortfolioResult likeWish(User user, Portfolio portfolio, Long portfolioId) {
        WishPortfolio newWish = WishPortfolio.create(user, portfolio);
        wishPortfolioRepository.save(newWish);
        return WishPortfolioResult.of(portfolioId, true);
    }

}
