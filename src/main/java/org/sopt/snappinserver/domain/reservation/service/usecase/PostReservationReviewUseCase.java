package org.sopt.snappinserver.domain.reservation.service.usecase;

import org.sopt.snappinserver.api.reservation.dto.request.CreateReservationReviewRequest;
import org.sopt.snappinserver.api.reservation.dto.response.CreateReservationReviewResponse;
import org.sopt.snappinserver.domain.reservation.service.dto.request.CreateReservationReviewCommand;
import org.sopt.snappinserver.domain.reservation.service.dto.response.CreateReservationReviewResult;

public interface PostReservationReviewUseCase {

    CreateReservationReviewResult createReservationReview(
        CreateReservationReviewCommand command
    );
}
