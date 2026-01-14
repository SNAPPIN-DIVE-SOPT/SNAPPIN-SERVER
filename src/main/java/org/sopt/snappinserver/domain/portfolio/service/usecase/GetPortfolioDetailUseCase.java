package org.sopt.snappinserver.domain.portfolio.service.usecase;

import org.sopt.snappinserver.domain.portfolio.service.dto.response.GetPortfolioDetailResult;

public interface GetPortfolioDetailUseCase {

    GetPortfolioDetailResult findPortfolioDetail(Long userId, Long portfolioId);
}
