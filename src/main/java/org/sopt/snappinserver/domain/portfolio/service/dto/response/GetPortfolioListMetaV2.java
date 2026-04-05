package org.sopt.snappinserver.domain.portfolio.service.dto.response;

public record GetPortfolioListMetaV2(
    boolean hasNext,
    String nextCursor
) {

}
