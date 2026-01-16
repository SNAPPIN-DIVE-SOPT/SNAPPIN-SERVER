package org.sopt.snappinserver.domain.portfolio.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.portfolio.repository.PortfolioRepositoryCustom;
import org.sopt.snappinserver.domain.portfolio.service.dto.request.GetPortfolioListQuery;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.GetPortfolioCardResult;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.GetPortfolioListMeta;
import org.sopt.snappinserver.domain.portfolio.service.dto.response.GetPortfolioListResult;
import org.sopt.snappinserver.domain.portfolio.service.usecase.GetPortfolioListUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class GetPortfolioListService implements GetPortfolioListUseCase {

    private static final int PAGE_SIZE = 30;

    private final PortfolioRepositoryCustom portfolioRepositoryCustom;

    @Override
    public GetPortfolioListResult getPortfolioList(GetPortfolioListQuery query) {

        List<GetPortfolioCardResult> rows =
            portfolioRepositoryCustom.findPortfolioCards(
                query.cursor(),
                query,
                PAGE_SIZE
            );

        boolean hasNext = rows.size() > PAGE_SIZE;
        List<GetPortfolioCardResult> portfolios = hasNext ? rows.subList(0, PAGE_SIZE) : rows;
        Long nextCursor = hasNext ? portfolios.get(portfolios.size() - 1).id() : null;

        return new GetPortfolioListResult(
            portfolios,
            new GetPortfolioListMeta(hasNext, nextCursor)
        );
    }

}
