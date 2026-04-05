package org.sopt.snappinserver.domain.review.repository;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.reservation.domain.entity.Reservation;
import org.sopt.snappinserver.domain.review.domain.entity.Review;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReservationReviewRepositoryImpl implements ReservationReviewRepository {

    private final ReviewRepository reviewRepository;

    @Override
    public boolean existsByReservationId(Long reservationId) {
        return reviewRepository.existsByReservation_Id(reservationId);
    }

    @Override
    public Optional<Review> findFirstByReservationOrderByIdDesc(Reservation reservation) {
        return reviewRepository.findFirstByReservationOrderByIdDesc(reservation);
    }

    @Override
    public List<Long> findReviewedReservationIds(List<Long> reservationIds) {
        return reviewRepository.findReviewedReservationIds(reservationIds);
    }

    @Override
    public Review save(Review review) {
        return reviewRepository.save(review);
    }
}
