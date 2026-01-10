package org.sopt.snappinserver.domain.product.service.usecase;

import java.time.LocalDate;
import org.sopt.snappinserver.domain.product.service.dto.response.ProductAvailableTimesResult;

public interface GetProductAvailableTimesUseCase {

    ProductAvailableTimesResult getProductAvailableTimes(Long productId, LocalDate date);
}
