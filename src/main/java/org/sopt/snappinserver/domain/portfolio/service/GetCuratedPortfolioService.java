package org.sopt.snappinserver.domain.portfolio.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.curation.domain.entity.Curation;
import org.sopt.snappinserver.domain.curation.repository.CurationRepository;
import org.sopt.snappinserver.domain.portfolio.domain.entity.Portfolio;
import org.sopt.snappinserver.domain.portfolio.domain.entity.PortfolioMood;
import org.sopt.snappinserver.domain.portfolio.domain.entity.PortfolioPhoto;
import org.sopt.snappinserver.domain.portfolio.domain.exception.PortfolioErrorCode;
import org.sopt.snappinserver.domain.portfolio.domain.exception.PortfolioException;
import org.sopt.snappinserver.domain.portfolio.repository.PortfolioMoodRepository;
import org.sopt.snappinserver.domain.portfolio.repository.PortfolioPhotoRepository;
import org.sopt.snappinserver.domain.portfolio.repository.PortfolioRepositoryCustom;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.GetCuratedPortfolioResult;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.GetImageResult;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.GetPortfolioResult;
import org.sopt.snappinserver.domain.portfolio.service.usecase.GetCuratedPortfolioUseCase;
import org.sopt.snappinserver.domain.portfolio.service.usecase.GetPopularPortfolioListUseCase;
import org.sopt.snappinserver.domain.user.domain.entity.User;
import org.sopt.snappinserver.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class GetCuratedPortfolioService implements GetCuratedPortfolioUseCase {

    private static final int PAGE_SIZE = 3;
    private static final int MAX_MATCH_COUNT = 3;
    private static final int LEAST_MATCH_COUNT = 1;

    private final UserRepository userRepository;
    private final CurationRepository curationRepository;
    private final PortfolioRepositoryCustom portfolioRepositoryCustom;
    private final PortfolioPhotoRepository portfolioPhotoRepository;
    private final PortfolioMoodRepository portfolioMoodRepository;
    private final GetPopularPortfolioListUseCase getPopularPortfolioListUseCase;

    @Value("${cloud.aws.cloud-front.domain}")
    private String cloudFrontDomain;

    @Override
    public GetCuratedPortfolioResult getCuratedPortfolio(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new PortfolioException(PortfolioErrorCode.USER_NOT_FOUND));

        List<Curation> curations =
            curationRepository.findLatestByUser(user, PageRequest.of(0, 3));

        curations.sort(Comparator.comparing(Curation::getRank));

        if (curations.isEmpty()) {
            return GetCuratedPortfolioResult.from(
                getPopularPortfolioListUseCase.getPopularPortfolioList()
            );
        }

        List<Long> curatedMoodIds = curations.stream()
            .map(c -> c.getMood().getId())
            .toList();

        List<Portfolio> portfolios = new ArrayList<>();
        Set<Long> excludedIds = new HashSet<>();

        for (int matchCount = MAX_MATCH_COUNT; matchCount >= LEAST_MATCH_COUNT; matchCount--) {
            if (portfolios.size() == PAGE_SIZE) {
                break;
            }

            List<Portfolio> found =
                portfolioRepositoryCustom.findByMatchCountExcludeIds(
                    curatedMoodIds,
                    excludedIds,
                    matchCount,
                    PAGE_SIZE - portfolios.size()
                );

            found.forEach(p -> {
                portfolios.add(p);
                excludedIds.add(p.getId());
            });
        }

        List<Long> portfolioIds = portfolios.stream()
            .map(Portfolio::getId)
            .toList();

        Map<Long, List<PortfolioPhoto>> photosByPortfolioId =
            portfolioPhotoRepository.findByPortfolioIds(portfolioIds).stream()
                .collect(Collectors.groupingBy(pp -> pp.getPortfolio().getId()));

        Map<Long, List<PortfolioMood>> moodsByPortfolioId =
            portfolioMoodRepository.findByPortfolioIds(portfolioIds).stream()
                .collect(Collectors.groupingBy(pm -> pm.getPortfolio().getId()));

        List<GetPortfolioResult> portfolioResults = portfolios.stream()
            .map(portfolio -> {
                List<PortfolioPhoto> photos = photosByPortfolioId.getOrDefault(
                    portfolio.getId(),
                    List.of()
                );

                List<GetImageResult> imageResults = photos.stream()
                    .map(portfolioPhoto -> GetImageResult.of(
                        cloudFrontDomain + portfolioPhoto.getPhoto().getImageUrl(),
                        portfolioPhoto.getDisplayOrder()
                    ))
                    .toList();

                List<PortfolioMood> moods = moodsByPortfolioId.getOrDefault(portfolio.getId(),
                    List.of());

                return GetPortfolioResult.of(portfolio, imageResults, moods);
            })
            .toList();

        return GetCuratedPortfolioResult.of(
            curations,
            portfolioResults
        );
    }
}
