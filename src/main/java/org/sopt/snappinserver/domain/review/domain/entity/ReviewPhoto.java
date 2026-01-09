package org.sopt.snappinserver.domain.review.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.snappinserver.domain.photo.domain.entity.Photo;
import org.sopt.snappinserver.global.entity.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ReviewPhoto extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "review_photo_seq_gen")
    @SequenceGenerator(
        name = "review_photo_seq_gen",
        sequenceName = "review_photo_seq",
        allocationSize = 1
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id", nullable = false)
    private Photo photo;

    @Column(nullable = false)
    private int displayOrder;

    @Builder(access = AccessLevel.PRIVATE)
    private ReviewPhoto(Review review, Photo photo, int displayOrder) {
        this.review = review;
        this.photo = photo;
        this.displayOrder = displayOrder;
    }

    public static ReviewPhoto create(Review review, Photo photo, int displayOrder) {
        return ReviewPhoto.builder()
            .review(review)
            .photo(photo)
            .displayOrder(displayOrder)
            .build();
    }

}
