package org.sopt.snappinserver.domain.reservation.service;

import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.photo.domain.entity.Photo;
import org.sopt.snappinserver.domain.photo.repository.PhotoRepository;
import org.sopt.snappinserver.domain.reservation.domain.entity.Reservation;
import org.sopt.snappinserver.domain.reservation.domain.enums.ReservationStatus;
import org.sopt.snappinserver.domain.reservation.domain.exception.ReservationErrorCode;
import org.sopt.snappinserver.domain.reservation.domain.exception.ReservationException;
import org.sopt.snappinserver.domain.reservation.repository.ReservationRepository;
import org.sopt.snappinserver.domain.reservation.service.dto.request.CreateReservationReviewCommand;
import org.sopt.snappinserver.domain.reservation.service.dto.response.CreateReservationReviewResult;
import org.sopt.snappinserver.domain.reservation.service.usecase.PostReservationReviewUseCase;
import org.sopt.snappinserver.domain.review.domain.entity.Review;
import org.sopt.snappinserver.domain.review.domain.entity.ReviewPhoto;
import org.sopt.snappinserver.domain.review.domain.exception.ReviewErrorCode;
import org.sopt.snappinserver.domain.review.domain.exception.ReviewException;
import org.sopt.snappinserver.domain.review.repository.ReviewPhotoRepository;
import org.sopt.snappinserver.domain.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostReservationReviewService implements PostReservationReviewUseCase {

    private final ReservationRepository reservationRepository;
    private final ReviewRepository reviewRepository;
    private final PhotoRepository photoRepository;
    private final ReviewPhotoRepository reviewPhotoRepository;

    @Override
    public CreateReservationReviewResult createReservationReview(
        CreateReservationReviewCommand command
    ) {
        Long userId = command.userId();
        Long reservationId = command.reservationId();

        Reservation reservation = getReservation(reservationId);

        validateReservationClient(reservation, userId);
        validateShootCompleted(reservation);
        validateReviewDuplicated(reservationId);

        Review review = createReview(command, reservation);
        reviewRepository.save(review);

        createAndSaveReviewPhotos(command, review);

        return CreateReservationReviewResult.of(review.getId(), reservationId);
    }

    private Reservation getReservation(Long reservationId) {
        return reservationRepository.findById(reservationId).orElseThrow(
            () -> new ReservationException(ReservationErrorCode.RESERVATION_NOT_FOUND)
        );
    }

    private void validateReservationClient(Reservation reservation, Long userId) {
        if (!reservation.getUser().getId().equals(userId)) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_USER_NOT_MATCH);
        }
    }

    private void validateShootCompleted(Reservation reservation) {
        if (reservation.getReservationStatus() != ReservationStatus.SHOOT_COMPLETED) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_NOT_COMPLETED);
        }
    }

    private void validateReviewDuplicated(Long reservationId) {
        if (reviewRepository.existsByReservationId(reservationId)) {
            throw new ReviewException(ReviewErrorCode.REVIEW_ALREADY_EXISTS);
        }
    }

    private Review createReview(CreateReservationReviewCommand command, Reservation reservation) {
        return Review.create(reservation, command.rating(), command.content());
    }

    private void createAndSaveReviewPhotos(CreateReservationReviewCommand command, Review review) {
        if (command.imageUrls() != null && !command.imageUrls().isEmpty()) {
            List<Photo> photos = command.imageUrls().stream().map(Photo::create).toList();
            photoRepository.saveAll(photos);

            List<ReviewPhoto> reviewPhotos = IntStream
                .range(0, photos.size())
                .mapToObj(i -> ReviewPhoto.create(review, photos.get(i), i + 1))
                .toList();
            reviewPhotoRepository.saveAll(reviewPhotos);
        }
    }
}

