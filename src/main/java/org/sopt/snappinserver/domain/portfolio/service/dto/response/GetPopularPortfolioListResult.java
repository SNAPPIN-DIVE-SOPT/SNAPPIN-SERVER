package org.sopt.snappinserver.domain.portfolio.service.dto.response;

import java.util.List;
import org.sopt.snappinserver.domain.mood.domain.entity.Mood;

public record GetPopularPortfolioListResult(
    List<String> popularMoods,
    List<GetPopularPortfolioResult> portfolios
) {

    public static GetPopularPortfolioListResult of(
        List<Mood> popularMoods,
        List<GetPopularPortfolioResult> portfolios
    ) {
        return new GetPopularPortfolioListResult(
            popularMoods.stream()
                .map(Mood::getName)
                .toList(),
            portfolios
        );
    }
}
