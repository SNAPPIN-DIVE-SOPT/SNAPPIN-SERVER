package org.sopt.snappinserver.domain.reservation.service.usecase;

import org.sopt.snappinserver.domain.reservation.service.dto.request.CreateReservationReviewCommand;
import org.sopt.snappinserver.domain.reservation.service.dto.response.CreateReservationReviewResult;

public interface PostReservationReviewUseCase {

    CreateReservationReviewResult createReservationReview(CreateReservationReviewCommand command);
}
