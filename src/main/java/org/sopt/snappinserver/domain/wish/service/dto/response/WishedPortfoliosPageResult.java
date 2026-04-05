package org.sopt.snappinserver.domain.wish.service.dto.response;

import java.util.List;

public record WishedPortfoliosPageResult(
    List<WishedPortfolioResult> portfolios,
    Long nextCursor,
    boolean hasNext
) {

    public static WishedPortfoliosPageResult from(
        List<WishedPortfolioResult> portfolios,
        Long nextCursor,
        boolean hasNext
    ) {
        return new WishedPortfoliosPageResult(portfolios, nextCursor, hasNext);
    }
}
