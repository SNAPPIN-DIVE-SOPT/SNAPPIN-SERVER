package org.sopt.snappinserver.domain.portfolio.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.mood.domain.entity.Mood;
import org.sopt.snappinserver.domain.mood.repository.MoodRepository;
import org.sopt.snappinserver.domain.portfolio.domain.entity.Portfolio;
import org.sopt.snappinserver.domain.portfolio.domain.entity.PortfolioMood;
import org.sopt.snappinserver.domain.portfolio.domain.entity.PortfolioPhoto;
import org.sopt.snappinserver.domain.portfolio.repository.PortfolioMoodRepository;
import org.sopt.snappinserver.domain.portfolio.repository.PortfolioPhotoRepository;
import org.sopt.snappinserver.domain.portfolio.repository.PortfolioRepository;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.GetPopularPortfolioListResult;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.GetPopularPortfolioResult;
import org.sopt.snappinserver.domain.portfolio.service.usecase.GetPopularPortfolioListUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class GetPopularPortfolioListService implements GetPopularPortfolioListUseCase {

    private static final int RANDOM_SIZE = 3;
    private static final int MIN_MATCH_COUNT = 1;

    private final MoodRepository moodRepository;
    private final PortfolioRepository portfolioRepository;
    private final PortfolioPhotoRepository portfolioPhotoRepository;
    private final PortfolioMoodRepository portfolioMoodRepository;

    @Override
    public GetPopularPortfolioListResult getPopularPortfolioList() {
        List<Mood> randomMoods = moodRepository.findRandom(RANDOM_SIZE);
        List<Long> moodIds = randomMoods.stream()
            .map(Mood::getId)
            .toList();

        List<Long> portfolioIds = getPopularPortfolioIds(moodIds);
        if (portfolioIds.isEmpty()) {
            return GetPopularPortfolioListResult.of(randomMoods, List.of());
        }

        List<Portfolio> portfolios = portfolioRepository.findAllByIdIn(portfolioIds);
        List<PortfolioPhoto> portfolioPhotos = portfolioPhotoRepository.findByPortfolioIds(
            portfolioIds
        );
        List<PortfolioMood> portfolioMoods = portfolioMoodRepository.findByPortfolioIds(
            portfolioIds
        );

        Map<Long, List<PortfolioPhoto>> photosByPortfolioId = getPortfolioPhotos(portfolioPhotos);
        Map<Long, List<Mood>> moodsByPortfolioId = getPortfolioMoods(portfolioMoods);
        List<GetPopularPortfolioResult> portfolioResults = getPortfolioResult(
            portfolios,
            photosByPortfolioId,
            moodsByPortfolioId
        );

        return GetPopularPortfolioListResult.of(randomMoods, portfolioResults);
    }

    private List<Long> getPopularPortfolioIds(List<Long> moodIds) {
        List<Long> portfolioIds = new ArrayList<>();

        for (int matchCount = RANDOM_SIZE; matchCount >= MIN_MATCH_COUNT; matchCount--) {
            if (portfolioIds.size() >= RANDOM_SIZE) {
                break;
            }

            int remain = RANDOM_SIZE - portfolioIds.size();
            portfolioIds.addAll(
                portfolioRepository.findIdsByMoodMatchCount(
                    moodIds,
                    matchCount,
                    remain
                )
            );
        }
        return portfolioIds;
    }

    private static Map<Long, List<PortfolioPhoto>> getPortfolioPhotos(
        List<PortfolioPhoto> portfolioPhotos) {
        return portfolioPhotos.stream()
            .collect(Collectors.groupingBy(
                pp -> pp.getPortfolio().getId()
            ));
    }

    private static Map<Long, List<Mood>> getPortfolioMoods(
        List<PortfolioMood> portfolioMoods) {
        return portfolioMoods.stream()
            .collect(Collectors.groupingBy(
                pm -> pm.getPortfolio().getId(),
                Collectors.mapping(PortfolioMood::getMood, Collectors.toList())
            ));
    }

    private static List<GetPopularPortfolioResult> getPortfolioResult(List<Portfolio> portfolios,
        Map<Long, List<PortfolioPhoto>> photosByPortfolioId,
        Map<Long, List<Mood>> moodsByPortfolioId
    ) {
        return portfolios.stream()
            .map(p -> GetPopularPortfolioResult.of(
                p,
                photosByPortfolioId.getOrDefault(p.getId(), List.of()),
                moodsByPortfolioId.getOrDefault(p.getId(), List.of())
            ))
            .toList();
    }
}
