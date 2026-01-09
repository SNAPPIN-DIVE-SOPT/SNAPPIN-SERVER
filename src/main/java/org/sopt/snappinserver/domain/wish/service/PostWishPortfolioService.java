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
    public WishPortfolioResult execute(Long userId, Long portfolioId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new WishException(WishErrorCode.USER_NOT_FOUND));

        Portfolio portfolio = portfolioRepository.findById(portfolioId)
            .orElseThrow(() -> new WishException(WishErrorCode.PORTFOLIO_NOT_FOUND));

        return wishPortfolioRepository.findByUserAndPortfolio(user, portfolio)
            .map(existing -> {
                wishPortfolioRepository.delete(existing);
                return WishPortfolioResult.of(portfolioId, false);
            })
            .orElseGet(() -> {
                WishPortfolio wish = WishPortfolio.create(user, portfolio);
                wishPortfolioRepository.save(wish);
                return WishPortfolioResult.of(portfolioId, true);
            });
    }
}