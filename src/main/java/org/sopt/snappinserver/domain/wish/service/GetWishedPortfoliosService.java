package org.sopt.snappinserver.domain.wish.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.portfolio.domain.entity.Portfolio;
import org.sopt.snappinserver.domain.portfolio.domain.entity.PortfolioPhoto;
import org.sopt.snappinserver.domain.portfolio.repository.PortfolioPhotoRepository;
import org.sopt.snappinserver.domain.user.domain.entity.User;
import org.sopt.snappinserver.domain.user.repository.UserRepository;
import org.sopt.snappinserver.domain.wish.domain.entity.WishPortfolio;
import org.sopt.snappinserver.domain.wish.domain.exception.WishErrorCode;
import org.sopt.snappinserver.domain.wish.domain.exception.WishException;
import org.sopt.snappinserver.domain.wish.repository.WishPortfolioRepository;
import org.sopt.snappinserver.domain.wish.service.dto.response.WishedPortfolioResult;
import org.sopt.snappinserver.domain.wish.service.dto.response.WishedPortfoliosResult;
import org.sopt.snappinserver.domain.wish.service.usecase.GetWishedPortfoliosUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetWishedPortfoliosService implements GetWishedPortfoliosUseCase {

    private final WishPortfolioRepository wishPortfolioRepository;
    private final PortfolioPhotoRepository portfolioPhotoRepository;
    private final UserRepository userRepository;

    @Value("${cloud.aws.cloud-front.domain}")
    private String cloudFrontDomain;

    @Override
    public WishedPortfoliosResult getWishedPortfolios(Long userId) {
        User user = getUser(userId);
        List<WishedPortfolioResult> results = getWishedPortfolioResults(user);

        return WishedPortfoliosResult.from(results);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new WishException(WishErrorCode.USER_NOT_FOUND));
    }

    private List<WishedPortfolioResult> getWishedPortfolioResults(User user) {
        return wishPortfolioRepository
            .findAllByUserOrderByCreatedAtDesc(user)
            .stream()
            .map(WishPortfolio::getPortfolio)
            .map(this::mapToWishedPortfolioResult)
            .toList();
    }

    private WishedPortfolioResult mapToWishedPortfolioResult(Portfolio portfolio) {
        String imageUrl = portfolioPhotoRepository
            .findFirstByPortfolioOrderByDisplayOrderAsc(portfolio)
            .map(PortfolioPhoto::getPhoto)
            .map(photo -> cloudFrontDomain + photo.getImageUrl())
            .orElse(null);

        return WishedPortfolioResult.of(portfolio.getId(), imageUrl);
    }
}
