package org.sopt.snappinserver.domain.portfolio.service.usecase;

import org.sopt.snappinserver.domain.portfolio.service.dto.request.GetPortfolioListQuery;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.GetPortfolioListResult;

public interface GetPortfolioListUseCase {

    GetPortfolioListResult getPortfolioList(GetPortfolioListQuery query);
}
