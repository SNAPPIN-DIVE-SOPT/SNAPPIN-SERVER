package org.sopt.snappinserver.domain.portfolio.service;

import java.util.ArrayList;
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
import org.sopt.snappinserver.domain.portfolio.service.usecase.GetCuratedPortfolioUseCase;
import org.sopt.snappinserver.domain.portfolio.service.usecase.GetPopularPortfolioListUseCase;
import org.sopt.snappinserver.domain.user.domain.entity.User;
import org.sopt.snappinserver.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetCuratedPortfolioService implements GetCuratedPortfolioUseCase {

    private static final int PAGE_SIZE = 3;

    private final UserRepository userRepository;
    private final CurationRepository curationRepository;
    private final PortfolioRepositoryCustom portfolioRepositoryCustom;
    private final PortfolioPhotoRepository portfolioPhotoRepository;
    private final PortfolioMoodRepository portfolioMoodRepository;
    private final GetPopularPortfolioListUseCase getPopularPortfolioListUseCase;

    @Override
    public GetCuratedPortfolioResult getCuratedPortfolio(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new PortfolioException(PortfolioErrorCode.USER_NOT_FOUND));

        List<Curation> curations = curationRepository
            .findTop3ByUserOrderByRankAscCreatedAtDesc(user);

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

        for (int matchCount = 3; matchCount >= 1; matchCount--) {
            if (portfolios.size() == PAGE_SIZE) break;

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
            portfolioPhotoRepository.findByPortfolioIds(
                    portfolios.stream().map(Portfolio::getId).toList()
                ).stream()
                .collect(Collectors.groupingBy(pp -> pp.getPortfolio().getId()));

        Map<Long, List<PortfolioMood>> moodsByPortfolioId =
            portfolioMoodRepository.findByPortfolioIds(portfolioIds).stream()
                .collect(Collectors.groupingBy(pm -> pm.getPortfolio().getId()));

        return GetCuratedPortfolioResult.of(
            curations,
            portfolios,
            photosByPortfolioId,
            moodsByPortfolioId
        );
    }
}


