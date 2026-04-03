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
import org.sopt.snappinserver.domain.wish.service.dto.response.WishedPortfoliosPageResult;
import org.sopt.snappinserver.domain.wish.service.dto.response.WishedPortfoliosResult;
import org.sopt.snappinserver.domain.wish.service.usecase.GetWishedPortfoliosUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetWishedPortfoliosService implements GetWishedPortfoliosUseCase {

    private static final int PAGE_SIZE = 10;
    private static final long MIN_CURSOR_VALUE = 1L;

    private final WishPortfolioRepository wishPortfolioRepository;
    private final PortfolioPhotoRepository portfolioPhotoRepository;
    private final UserRepository userRepository;

    @Value("${cloud.aws.cloud-front.domain}")
    private String cloudFrontDomain;

    @Override
    public WishedPortfoliosResult getWishedPortfolios(Long userId) {
        User user = getUser(userId);
        List<WishPortfolio> wishes =
            wishPortfolioRepository.findAllByUserOrderByCreatedAtDesc(user);
        List<WishedPortfolioResult> results = mapWishesToResults(wishes);

        return WishedPortfoliosResult.from(results);
    }

    @Override
    public WishedPortfoliosPageResult getWishedPortfoliosPage(Long userId, Long cursor) {
        User user = getUser(userId);
        validateCursor(cursor);

        Pageable pageable = PageRequest.of(0, PAGE_SIZE + 1);
        List<WishPortfolio> wishes =
            (cursor == null)
                ? wishPortfolioRepository.findAllByUserWithPortfolioOrderByIdDesc(user, pageable)
                : wishPortfolioRepository.findAllByUserWithPortfolioOrderByIdDescAndCursor(
                    user,
                    cursor,
                    pageable
                );

        boolean hasNext = wishes.size() > PAGE_SIZE;
        if (hasNext) {
            wishes = wishes.subList(0, PAGE_SIZE);
        }

        if (wishes.isEmpty()) {
            return WishedPortfoliosPageResult.from(List.of(), null, false);
        }

        List<WishedPortfolioResult> results = mapWishesToResults(wishes);

        Long nextCursor = hasNext ? wishes.get(wishes.size() - 1).getId() : null;

        return WishedPortfoliosPageResult.from(results, nextCursor, hasNext);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new WishException(WishErrorCode.USER_NOT_FOUND));
    }

    private List<WishedPortfolioResult> mapWishesToResults(List<WishPortfolio> wishes) {
        return wishes.stream()
            .map(WishPortfolio::getPortfolio)
            .map(this::mapToWishedPortfolioResult)
            .toList();
    }

    private static void validateCursor(Long cursor) {
        if (cursor != null && cursor < MIN_CURSOR_VALUE) {
            throw new WishException(WishErrorCode.INVALID_CURSOR);
        }
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
