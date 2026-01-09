package org.sopt.snappinserver.domain.product.service.dto.response;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public record ProductClosedDatesResult(
    List<LocalDate> closedDates
) {

    public static ProductClosedDatesResult of(List<LocalDate> closedDates) {
        return new ProductClosedDatesResult(
            closedDates == null ? Collections.emptyList() : List.copyOf(closedDates)
        );
    }
}
