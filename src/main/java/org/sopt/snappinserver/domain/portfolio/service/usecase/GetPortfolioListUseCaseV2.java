package org.sopt.snappinserver.domain.portfolio.service.usecase;

import org.sopt.snappinserver.domain.portfolio.service.dto.request.GetPortfolioListQueryV2;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.GetPortfolioListResultV2;

public interface GetPortfolioListUseCaseV2 {

    GetPortfolioListResultV2 getPortfolioList(GetPortfolioListQueryV2 query);
}
