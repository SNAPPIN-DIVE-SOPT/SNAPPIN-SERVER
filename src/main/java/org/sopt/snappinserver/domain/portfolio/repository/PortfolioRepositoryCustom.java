package org.sopt.snappinserver.domain.portfolio.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.sopt.snappinserver.domain.mood.domain.enums.MoodCategory;
import org.sopt.snappinserver.domain.portfolio.domain.entity.Portfolio;
import org.sopt.snappinserver.domain.portfolio.service.dto.request.GetPortfolioListQuery;
import org.sopt.snappinserver.domain.portfolio.service.dto.request.GetPortfolioListQueryV2;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.GetPortfolioCardResult;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.GetPortfolioCardResultV2;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.LikeStatusProjection;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.PortfolioDetailProjection;
import org.sopt.snappinserver.global.enums.SnapCategory;

public interface PortfolioRepositoryCustom {

    Optional<String> findBestPortfolioImageByPlaceId(Long placeId);

    PortfolioDetailProjection findDetail(Long portfolioId);

    LikeStatusProjection findLikeStatus(Long portfolioId, Long userId);

    List<String> findPortfolioImageUrls(Long portfolioId);

    List<String> findPortfolioMoods(Long portfolioId);

    String findProductThumbnailUrl(Long productId);

    List<SnapCategory> findPhotographerSpecialties(Long photographerId);

    List<String> findPhotographerAvailableLocations(Long photographerId);

    List<Portfolio> findByMatchCountExcludeIds(
        List<Long> curatedMoodIds,
        Set<Long> excludedPortfolioIds,
        int matchCount,
        int limit
    );

    List<GetPortfolioCardResult> findPortfolioCards(
        Long cursor,
        GetPortfolioListQuery query,
        Map<MoodCategory, List<Long>> moodGroupMap,
        int size
    );

    List<GetPortfolioCardResultV2> findPortfolioCardsV2(
        GetPortfolioListQueryV2 query,
        Map<MoodCategory, List<Long>> moodGroupMap,
        int size
    );

    long countPortfolioCardsV2(
        GetPortfolioListQueryV2 query,
        Map<MoodCategory, List<Long>> moodGroupMap
    );
}
