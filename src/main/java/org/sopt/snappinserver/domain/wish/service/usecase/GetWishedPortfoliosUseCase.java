package org.sopt.snappinserver.domain.wish.service.usecase;

import org.sopt.snappinserver.domain.wish.service.dto.response.WishedPortfoliosResult;

public interface GetWishedPortfoliosUseCase {
    WishedPortfoliosResult getWishedPortfolios(Long userId);
}
