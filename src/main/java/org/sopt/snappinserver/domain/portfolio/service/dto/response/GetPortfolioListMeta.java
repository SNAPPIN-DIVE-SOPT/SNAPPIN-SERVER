package org.sopt.snappinserver.domain.portfolio.service.dto.response;

public record GetPortfolioListMeta(
    Boolean hasNext,
    Long nextCursor
) {

}
