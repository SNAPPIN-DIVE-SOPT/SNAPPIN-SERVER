package org.sopt.snappinserver.domain.portfolio.repository;

import java.util.List;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.LikeStatusProjection;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.PortfolioDetailProjection;

public interface PortfolioRepositoryCustom {

    PortfolioDetailProjection findDetail(Long portfolioId);

    LikeStatusProjection findLikeStatus(Long portfolioId, Long userId);

    List<String> findPortfolioImageUrls(Long portfolioId);

    List<String> findPortfolioMoods(Long portfolioId);

    List<String> findProductMoods(Long productId);

    String findProductThumbnailUrl(Long productId);

    List<String> findPhotographerSpecialties(Long photographerId);

    List<String> findPhotographerAvailableLocations(Long photographerId);
}
