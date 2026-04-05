package org.sopt.snappinserver.domain.review.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.snappinserver.domain.product.domain.entity.Product;
import org.sopt.snappinserver.domain.reservation.domain.entity.Reservation;
import org.sopt.snappinserver.domain.review.domain.exception.ReviewErrorCode;
import org.sopt.snappinserver.domain.review.domain.exception.ReviewException;
import org.sopt.snappinserver.domain.user.domain.entity.User;
import org.sopt.snappinserver.global.entity.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Review extends BaseEntity {

    private static final int MIN_RATING_SCORE = 1;
    private static final int MAX_RATING_SCORE = 5;
    private static final int MAX_CONTENT_LENGTH = 512;
    private static final String UNKNOWN_REVIEWER_NAME = "알 수 없음";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "review_seq_gen")
    @SequenceGenerator(
        name = "review_seq_gen",
        sequenceName = "review_seq",
        allocationSize = 1
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /** 과거 예약 기반 리뷰만 연결 - 상품만으로 작성한 리뷰는 null */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = true)
    private Reservation reservation;

    @Column(nullable = false)
    private Integer rating;

    @Column(nullable = false, length = MAX_CONTENT_LENGTH)
    private String content;

    @Builder(access = AccessLevel.PRIVATE)
    private Review(
        Product product,
        User user,
        Reservation reservation,
        Integer rating,
        String content
    ) {
        this.product = product;
        this.user = user;
        this.reservation = reservation;
        this.rating = rating;
        this.content = content;
    }

    public static Review create(User author, Product product, Integer rating, String content) {
        validateReview(product, rating, content);
        return Review.builder()
            .user(author)
            .product(product)
            .reservation(null)
            .rating(rating)
            .content(content)
            .build();
    }

    /**
     * v1 예약 기반 리뷰 등록용. 예약의 상품·예약자 정보를 함께 저장합니다.
     */
    public static Review createForReservation(Reservation reservation, Integer rating, String content) {
        if (reservation == null) {
            throw new ReviewException(ReviewErrorCode.RESERVATION_REQUIRED);
        }
        User author = reservation.getUser();
        Product product = reservation.getProduct();
        validateReview(product, rating, content);
        return Review.builder()
            .user(author)
            .product(product)
            .reservation(reservation)
            .rating(rating)
            .content(content)
            .build();
    }

    public User resolveReviewer() {
        if (user != null) {
            return user;
        }
        return reservation != null ? reservation.getUser() : null;
    }

    public String resolveReviewerName() {
        User reviewer = resolveReviewer();
        return reviewer != null ? reviewer.getName() : UNKNOWN_REVIEWER_NAME;
    }

    private static void validateReview(Product product, Integer rating, String content) {
        validateProduct(product);
        validateRating(rating);
        validateContent(content);
    }

    private static void validateProduct(Product product) {
        if (product == null) {
            throw new ReviewException(ReviewErrorCode.REVIEW_PRODUCT_REQUIRED);
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
