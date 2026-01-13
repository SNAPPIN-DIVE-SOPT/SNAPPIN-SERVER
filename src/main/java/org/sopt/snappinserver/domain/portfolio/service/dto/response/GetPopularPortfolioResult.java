package org.sopt.snappinserver.domain.portfolio.service.dto.response;

import java.util.Comparator;
import java.util.List;
import org.sopt.snappinserver.domain.mood.domain.entity.Mood;
import org.sopt.snappinserver.domain.portfolio.domain.entity.Portfolio;
import org.sopt.snappinserver.domain.portfolio.domain.entity.PortfolioPhoto;

public record GetPopularPortfolioResult(
    Long id,
    List<GetImageResult> images,
    List<String> moods,
    String photographerName
) {

    public static GetPopularPortfolioResult of(
        Portfolio portfolio,
        List<PortfolioPhoto> photos,
        List<Mood> moods
    ) {
        return new GetPopularPortfolioResult(
            portfolio.getId(),

            photos.stream()
                .sorted(Comparator.comparingInt(PortfolioPhoto::getDisplayOrder))
                .map(GetImageResult::from)
                .toList(),

            moods.stream()
                .map(Mood::getName)
                .toList(),

            portfolio.getProduct().getPhotographer().getNickname()
        );
    }
}


