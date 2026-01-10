package org.sopt.snappinserver.domain.product.service.dto.request;

import java.time.LocalDateTime;
import org.sopt.snappinserver.api.product.dto.request.ProductReservationRequest;

public record ProductReservationCommand(
    LocalDateTime reservedAt,
    Integer durationTime,
    String place,
    Integer peopleCount,
    String requestNote
) {
    public static ProductReservationCommand from(ProductReservationRequest request) {
        return new ProductReservationCommand(
            LocalDateTime.of(request.date(), request.startTime()),
            request.durationTime(),
            request.place(),
            request.peopleCount(),
            request.requestNote()
        );
    }
}
