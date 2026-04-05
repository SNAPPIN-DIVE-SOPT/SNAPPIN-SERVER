package org.sopt.snappinserver.domain.product.service.usecase;

import org.sopt.snappinserver.domain.product.service.dto.request.GetProductListQueryV2;
import org.sopt.snappinserver.domain.product.service.dto.response.GetProductListResultV2;

public interface GetProductListUseCaseV2 {

    GetProductListResultV2 getProductList(GetProductListQueryV2 query);
}
