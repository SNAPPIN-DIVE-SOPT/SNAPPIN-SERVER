package org.sopt.snappinserver.domain.wish.service.usecase;

import org.sopt.snappinserver.domain.wish.service.dto.response.WishedProductsPageResult;
import org.sopt.snappinserver.domain.wish.service.dto.response.WishedProductsResult;

public interface GetWishedProductsUseCase {

    WishedProductsResult getWishedProducts(Long userId);

    WishedProductsPageResult getWishedProductsPage(Long userId, Long cursor);

}
