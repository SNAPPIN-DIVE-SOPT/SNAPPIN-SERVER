package org.sopt.snappinserver.domain.curation.domain.entity;

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
import org.sopt.snappinserver.domain.curation.domain.exception.CurationErrorCode;
import org.sopt.snappinserver.domain.curation.domain.exception.CurationException;
import org.sopt.snappinserver.domain.mood.domain.entity.Mood;
import org.sopt.snappinserver.domain.user.domain.entity.User;
import org.sopt.snappinserver.global.entity.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Curation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mood_id")
    private Mood mood;

    @Column(nullable = false)
    private Integer rank;

    @Builder(access = AccessLevel.PROTECTED)
    private Curation(User user, Mood mood, Integer rank) {
        this.user = user;
        this.mood = mood;
        this.rank = rank;
    }

    public static Curation create(User user, Mood mood, Integer rank) {
        validateCurationMood(user, mood, rank);
        return Curation.builder()
            .user(user)
            .mood(mood)
            .rank(rank)
            .build();
    }

    private static void validateCurationMood(User user, Mood mood, Integer rank) {
        validateUserExists(user);
        validateMoodExists(mood);
        validateRankExists(rank);
    }

    private static void validateUserExists(User user) {
        if (user == null) {
            throw new CurationException(CurationErrorCode.USER_REQUIRED);
        }
    }

    private static void validateMoodExists(Mood mood) {
        if (mood == null) {
            throw new CurationException(CurationErrorCode.MOOD_REQUIRED);
        }
    }

    private static void validateRankExists(Integer rank) {
        if (rank == null) {
            throw new CurationException(CurationErrorCode.RANK_REQUIRED);
        }
    }

}
