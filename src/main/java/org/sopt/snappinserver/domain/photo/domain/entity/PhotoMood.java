package org.sopt.snappinserver.domain.photo.domain.entity;

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
import org.sopt.snappinserver.domain.mood.domain.entity.Mood;
import org.sopt.snappinserver.global.entity.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PhotoMood extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "photo_mood_seq_gen")
    @SequenceGenerator(
        name = "photo_mood_seq_gen",
        sequenceName = "photo_mood_seq",
        allocationSize = 1
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id")
    private Photo photo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mood_id")
    private Mood mood;

    private int rank;

    private float score;

    @Builder(access = AccessLevel.PRIVATE)
    private PhotoMood(Photo photo, Mood mood, int rank, float score) {
        this.photo = photo;
        this.mood = mood;
        this.rank = rank;
        this.score = score;
    }

    public static PhotoMood create(Photo photo, Mood mood, int rank, float score) {
        return PhotoMood.builder()
            .photo(photo)
            .mood(mood)
            .rank(rank)
            .score(score)
            .build();
    }

}
