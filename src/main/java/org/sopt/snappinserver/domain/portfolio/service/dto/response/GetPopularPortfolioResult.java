package org.sopt.snappinserver.domain.portfolio.service.dto.response;

import java.util.List;
import org.sopt.snappinserver.domain.mood.domain.entity.Mood;
import org.sopt.snappinserver.domain.portfolio.domain.entity.Portfolio;

public record GetPopularPortfolioResult(
    Long id,
    List<GetImageResult> images,
    List<String> moods,
    String photographerName
) {

    public static GetPopularPortfolioResult of(
        Portfolio portfolio,
        List<GetImageResult> images,
        List<Mood> moods
    ) {
        return new GetPopularPortfolioResult(
            portfolio.getId(),
            images,
            moods.stream().map(Mood::getName).toList(),
            portfolio.getProduct().getPhotographer().getNickname()
        );
    }
}
