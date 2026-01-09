package org.sopt.snappinserver.domain.product.service.usecase;

import java.time.YearMonth;
import org.sopt.snappinserver.domain.product.service.dto.response.ProductClosedDatesResult;

public interface GetProductClosedDatesUseCase {

    ProductClosedDatesResult getProductClosedDates(
        Long productId,
        YearMonth yearMonth
    );
}
