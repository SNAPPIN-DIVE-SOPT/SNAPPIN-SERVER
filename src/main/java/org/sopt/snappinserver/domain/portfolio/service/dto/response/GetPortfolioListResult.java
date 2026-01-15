package org.sopt.snappinserver.domain.portfolio.service.dto.response;

import java.util.List;

public record GetPortfolioListResult(
    List<GetPortfolioCardResult> portfolios,
    GetPortfolioListMeta getPortfolioListMeta
) {

}
