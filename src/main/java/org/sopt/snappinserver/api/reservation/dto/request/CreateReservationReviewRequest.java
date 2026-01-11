package org.sopt.snappinserver.api.reservation.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CreateReservationReviewRequest(
    @NotNull
    @Min(1)
    @Max(5)
    Integer rating,

    @NotBlank
    String content,

    List<String> imageUrls
) {
}
