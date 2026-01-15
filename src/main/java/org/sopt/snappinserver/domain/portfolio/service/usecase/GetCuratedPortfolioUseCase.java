package org.sopt.snappinserver.domain.portfolio.service.usecase;

import org.sopt.snappinserver.domain.portfolio.service.dto.response.GetCuratedPortfolioResult;

public interface GetCuratedPortfolioUseCase {

    GetCuratedPortfolioResult getCuratedPortfolio(Long userId);
}
