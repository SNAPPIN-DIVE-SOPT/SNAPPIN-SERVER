package org.sopt.snappinserver.domain.product.service.usecase;

import org.sopt.snappinserver.domain.product.service.dto.response.GetProductExtraInfoResult;

public interface GetProductExtraInfoUseCase {

    GetProductExtraInfoResult getProductExtraInfo(Long productId);
}
