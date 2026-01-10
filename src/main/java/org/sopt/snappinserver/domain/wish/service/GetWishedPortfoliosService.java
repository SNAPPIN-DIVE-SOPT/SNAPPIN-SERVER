package org.sopt.snappinserver.domain.wish.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.portfolio.domain.entity.Portfolio;
import org.sopt.snappinserver.domain.user.domain.entity.User;
import org.sopt.snappinserver.domain.user.repository.UserRepository;
import org.sopt.snappinserver.domain.wish.domain.entity.WishPortfolio;
import org.sopt.snappinserver.domain.wish.repository.WishPortfolioRepository;
import org.sopt.snappinserver.domain.wish.service.dto.response.WishedPortfolioResult;
import org.sopt.snappinserver.domain.wish.service.dto.response.WishedPortfoliosResult;
import org.sopt.snappinserver.domain.wish.service.usecase.GetWishedPortfoliosUseCase;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetWishedPortfoliosService implements GetWishedPortfoliosUseCase {

    private final WishPortfolioRepository wishPortfolioRepository;
    private final UserRepository userRepository;

    @Override
    public WishedPortfoliosResult getWishedPortfolios(Long userId) {
        User user = userRepository.getById(userId);

        List<WishedPortfolioResult> results =
            wishPortfolioRepository.findAllByUser(user)
                .stream()
                .map(WishPortfolio::getPortfolio)
                .map(WishedPortfolioResult::from)
                .toList();

        return WishedPortfoliosResult.from(results);
    }
}

