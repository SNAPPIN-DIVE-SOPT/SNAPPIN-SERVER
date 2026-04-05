package org.sopt.snappinserver.domain.portfolio.service.dto.response;

import java.util.List;

public record GetPortfolioListResultV2(
    List<GetPortfolioCardResultV2> portfolios,
    GetPortfolioListMeta meta
) {

}
