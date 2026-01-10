package org.sopt.snappinserver.domain.wish.service.dto.response;

import java.util.List;

public record WishedPortfoliosResult(List<WishedPortfolioResult> portfolios) {

    public static WishedPortfoliosResult from(List<WishedPortfolioResult> portfolios) {
        return new WishedPortfoliosResult(portfolios);
    }
}
