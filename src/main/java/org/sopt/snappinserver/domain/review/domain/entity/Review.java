package org.sopt.snappinserver.domain.review.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.snappinserver.domain.reservation.domain.entity.Reservation;
import org.sopt.snappinserver.domain.review.domain.exception.ReviewErrorCode;
import org.sopt.snappinserver.domain.review.domain.exception.ReviewException;
import org.sopt.snappinserver.global.entity.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Review extends BaseEntity {

    private static final int MIN_RATING_SCORE = 1;
    private static final int MAX_RATING_SCORE = 5;
    private static final int MAX_CONTENT_LENGTH = 512;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false, unique = true)
    private Reservation reservation;

    @Column(nullable = false)
    private Integer rating;

    @Column(nullable = false, length = MAX_CONTENT_LENGTH)
    private String content;

    @OneToMany(mappedBy = "review", fetch = FetchType.LAZY)
    private List<ReviewPhoto> reviewPhotos = new ArrayList<>();

    @Builder(access = AccessLevel.PRIVATE)
    private Review(Reservation reservation, Integer rating, String content) {
        this.reservation = reservation;
        this.rating = rating;
        this.content = content;
    }

    public static Review create(Reservation reservation, Integer rating, String content) {
        validateReview(reservation, rating, content);
        return Review.builder()
            .reservation(reservation)
            .rating(rating)
            .content(content)
            .build();
    }

    private static void validateReview(Reservation reservation, Integer rating, String content) {
        validateReservationExists(reservation);
        validateRating(rating);
        validateContent(content);
    }

    private static void validateReservationExists(Reservation reservation) {
        if (reservation == null) {
            throw new ReviewException(ReviewErrorCode.RESERVATION_REQUIRED);
        }
    }

    private static void validateRating(Integer rating) {
        validateRatingExists(rating);
        validateRatingRange(rating);
    }

    private static void validateRatingExists(Integer rating) {
        if (rating == null) {
            throw new ReviewException(ReviewErrorCode.RATING_REQUIRED);
        }
    }

    private static void validateRatingRange(Integer rating) {
        validateMinRating(rating);
        validateMaxRating(rating);
    }

    private static void validateMinRating(Integer rating) {
        if (rating < MIN_RATING_SCORE) {
            throw new ReviewException(ReviewErrorCode.RATING_SCORE_TOO_SMALL);
        }
    }

    private static void validateMaxRating(Integer rating) {
        if (rating > MAX_RATING_SCORE) {
            throw new ReviewException(ReviewErrorCode.RATING_SCORE_TOO_BIG);
        }
    }

    private static void validateContent(String content) {
        validateContentExists(content);
        validateContentLength(content);
    }

    private static void validateContentExists(String content) {
        if (content == null) {
            throw new ReviewException(ReviewErrorCode.CONTENT_REQUIRED);
        }
    }

    private static void validateContentLength(String content) {
        if (content.length() > MAX_CONTENT_LENGTH) {
            throw new ReviewException(ReviewErrorCode.CONTENT_TOO_LONG);
        }
    }

}
