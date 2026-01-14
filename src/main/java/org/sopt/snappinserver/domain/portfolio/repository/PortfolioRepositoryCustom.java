package org.sopt.snappinserver.domain.portfolio.repository;

import java.util.Optional;

public interface PortfolioRepositoryCustom {

    Optional<String> findBestPortfolioImageByPlaceId(Long placeId);
}
