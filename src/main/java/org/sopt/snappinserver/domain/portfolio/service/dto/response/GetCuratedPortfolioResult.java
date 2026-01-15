package org.sopt.snappinserver.domain.portfolio.service.dto.response;

import java.util.List;
import java.util.Map;
import org.sopt.snappinserver.domain.curation.domain.entity.Curation;
import org.sopt.snappinserver.domain.mood.domain.entity.Mood;
import org.sopt.snappinserver.domain.portfolio.domain.entity.Portfolio;
import org.sopt.snappinserver.domain.portfolio.domain.entity.PortfolioMood;
import org.sopt.snappinserver.domain.portfolio.domain.entity.PortfolioPhoto;

public record GetCuratedPortfolioResult(
    List<String> curatedMoods,
    List<GetPortfolioResult> portfolios
) {

    public static GetCuratedPortfolioResult from(GetPopularPortfolioListResult result) {
        return new GetCuratedPortfolioResult(
            result.popularMoods(),
            result.portfolios().stream()
                .map(GetPortfolioResult::from)
                .toList()
        );
    }

    public static GetCuratedPortfolioResult of(
        List<Curation> curations,
        List<Portfolio> portfolios,
        Map<Long, List<PortfolioPhoto>> photosByPortfolioId,
        Map<Long, List<PortfolioMood>> moodsByPortfolioId
    ) {
        return new GetCuratedPortfolioResult(
            curations.stream()
                .map(Curation::getMood)
                .map((Mood::getName))
                .toList(),
            portfolios.stream()
                .map(p -> GetPortfolioResult.of(
                    p,
                    photosByPortfolioId.getOrDefault(p.getId(), List.of()),
                    moodsByPortfolioId.getOrDefault(p.getId(), List.of())
                ))
                .toList()
        );
    }
}
