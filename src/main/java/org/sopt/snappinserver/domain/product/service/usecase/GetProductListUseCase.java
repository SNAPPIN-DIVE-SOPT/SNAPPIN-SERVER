package org.sopt.snappinserver.domain.product.service.usecase;

import org.sopt.snappinserver.domain.product.service.dto.request.GetProductListQuery;
import org.sopt.snappinserver.domain.product.service.dto.response.GetProductListResult;

public interface GetProductListUseCase {

    GetProductListResult getProductList(GetProductListQuery listQuery);
}
