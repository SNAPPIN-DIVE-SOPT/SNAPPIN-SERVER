package org.sopt.snappinserver.domain.product.service.dto.response;

import java.time.LocalDate;
import java.util.List;

public record ProductAvailableTimesResult(
    LocalDate date,
    List<ProductAvailableTimeResult> times
) {
}
