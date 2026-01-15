package org.sopt.snappinserver.domain.portfolio.service.dto.response;

import java.util.List;
import org.sopt.snappinserver.domain.mood.domain.entity.Mood;
import org.sopt.snappinserver.domain.portfolio.domain.entity.Portfolio;
import org.sopt.snappinserver.domain.portfolio.domain.entity.PortfolioMood;
import org.sopt.snappinserver.domain.portfolio.domain.entity.PortfolioPhoto;

public record GetPortfolioResult(
    Long id,
    List<GetImageResult> images,
    List<String> moods,
    String photographerName
) {

    public static GetPortfolioResult from(GetPopularPortfolioResult result) {
        return new GetPortfolioResult(
            result.id(),
            result.images(),
            result.moods(),
            result.photographerName()
        );
    }

    public static GetPortfolioResult of(
        Portfolio portfolio,
        List<PortfolioPhoto> portfolioPhotos,
        List<PortfolioMood> portfolioMoods
    ) {
        return new GetPortfolioResult(
            portfolio.getId(),
            portfolioPhotos.stream()
                .map(GetImageResult::from)
                .toList(),
            portfolioMoods.stream()
                .map(PortfolioMood::getMood)
                .map(Mood::getName)
                .toList(),
            portfolio.getProduct().getPhotographer().getNickname()
        );
    }

}
