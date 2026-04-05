package org.sopt.snappinserver.domain.review.repository;

import java.util.List;
import java.util.Optional;
import org.sopt.snappinserver.domain.reservation.domain.entity.Reservation;
import org.sopt.snappinserver.domain.review.domain.entity.Review;

/**
 * 예약 기반 리뷰 등록 전용
 * 실제 저장·조회는 ReviewRepository에 위임
 */
public interface ReservationReviewRepository {

    boolean existsByReservationId(Long reservationId);

    Optional<Review> findFirstByReservationOrderByIdDesc(Reservation reservation);

    List<Long> findReviewedReservationIds(List<Long> reservationIds);

    Review save(Review review);
}
