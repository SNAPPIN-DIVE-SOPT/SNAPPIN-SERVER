package org.sopt.snappinserver.api.wish.dto.response;

import java.util.List;
import org.sopt.snappinserver.domain.wish.service.dto.response.WishedPortfoliosResult;

public record WishedPortfoliosResponse(List<WishedPortfolioResponse> portfolios) {

    public static WishedPortfoliosResponse from(WishedPortfoliosResult result) {
        return new WishedPortfoliosResponse(
            result.portfolios().stream().map(WishedPortfolioResponse::from).toList()
        );
    }
}
