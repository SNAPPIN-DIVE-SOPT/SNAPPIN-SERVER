package org.sopt.snappinserver.domain.product.service.dto.request;

import java.time.LocalDateTime;
import org.sopt.snappinserver.api.v1.product.dto.request.ProductReservationRequest;
import org.sopt.snappinserver.domain.product.domain.exception.ProductErrorCode;
import org.sopt.snappinserver.domain.product.domain.exception.ProductException;

public record ProductReservationCommand(
    LocalDateTime reservedAt,
    Integer durationTime,
    Long placeId,
    Integer peopleCount,
    String requestNote
) {

    public static ProductReservationCommand from(ProductReservationRequest request) {
        validateReservation(request);

        int durationMinutes = (int) (request.durationTime() * 60);

        return new ProductReservationCommand(
            LocalDateTime.of(request.date(), request.startTime()),
            durationMinutes,
            request.placeId(),
            request.peopleCount(),
            request.requestNote()
        );
    }

    private static void validateReservation(ProductReservationRequest request) {
        if (request.durationTime() == null
            || request.durationTime() <= 0
            || request.durationTime() % 0.5 != 0
        ) {
            throw new ProductException(ProductErrorCode.DURATION_TIME_REQUIRED);
        }

        if (request.peopleCount() == null || request.peopleCount() <= 0) {
            throw new ProductException(ProductErrorCode.PRODUCT_PEOPLE_RANGE_NOT_FOUND);
        }
    }
}
