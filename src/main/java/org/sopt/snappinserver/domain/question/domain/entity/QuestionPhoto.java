package org.sopt.snappinserver.domain.question.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.snappinserver.domain.photo.domain.entity.Photo;
import org.sopt.snappinserver.global.entity.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class QuestionPhoto extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id")
    private Photo photo;

    @Column(nullable = false)
    private int displayOrder;

    @Builder(access = AccessLevel.PRIVATE)
    private QuestionPhoto(Question question, Photo photo, int displayOrder) {
        this.question = question;
        this.photo = photo;
        this.displayOrder = displayOrder;
    }

    public static QuestionPhoto create(Question question, Photo photo, int displayOrder) {
        return QuestionPhoto.builder()
            .question(question)
            .photo(photo)
            .displayOrder(displayOrder)
            .build();
    }
}
