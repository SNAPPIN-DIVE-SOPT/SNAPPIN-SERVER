package org.sopt.snappinserver.domain.portfolio.service.dto.response;

import java.util.List;
import org.sopt.snappinserver.domain.curation.domain.entity.Curation;
import org.sopt.snappinserver.domain.mood.domain.entity.Mood;

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
        List<GetPortfolioResult> portfolioResults
    ) {
        return new GetCuratedPortfolioResult(
            curations.stream()
                .map(Curation::getMood)
                .map(Mood::getName)
                .toList(),
            portfolioResults
        );
    }
}
