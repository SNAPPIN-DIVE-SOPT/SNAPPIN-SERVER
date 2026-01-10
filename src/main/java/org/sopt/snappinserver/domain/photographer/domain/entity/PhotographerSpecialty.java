package org.sopt.snappinserver.domain.photographer.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.snappinserver.domain.photographer.domain.exception.PhotographerErrorCode;
import org.sopt.snappinserver.domain.photographer.domain.exception.PhotographerException;
import org.sopt.snappinserver.global.entity.BaseEntity;
import org.sopt.snappinserver.global.enums.SnapCategory;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    name = "photographer_specialty",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_photographer_specialty",
            columnNames = {"photographer_id", "specialty"}
        )
    }
)
public class PhotographerSpecialty extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "photographer_specialty_seq_gen")
    @SequenceGenerator(
        name = "photographer_specialty_seq_gen",
        sequenceName = "photographer_specialty_seq",
        allocationSize = 1
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photographer_id", nullable = false)
    private Photographer photographer;

    @Enumerated(EnumType.STRING)
    @Column(name = "specialty", nullable = false)
    private SnapCategory specialty;

    @Builder(access = AccessLevel.PRIVATE)
    private PhotographerSpecialty(Photographer photographer, SnapCategory specialty) {
        this.photographer = photographer;
        this.specialty = specialty;
    }

    public static PhotographerSpecialty create(Photographer photographer, SnapCategory specialty) {
        validatePhotographerSpecialty(photographer, specialty);

        return PhotographerSpecialty.builder()
            .photographer(photographer)
            .specialty(specialty)
            .build();
    }

    private static void validatePhotographerSpecialty(
        Photographer photographer,
        SnapCategory specialty
    ) {
        validatePhotographerExists(photographer);
        validateSpecialtyExists(specialty);
    }

    private static void validatePhotographerExists(Photographer photographer) {
        if (photographer == null) {
            throw new PhotographerException(PhotographerErrorCode.PHOTOGRAPHER_REQUIRED);
        }
    }

    private static void validateSpecialtyExists(SnapCategory specialty) {
        if (specialty == null) {
            throw new PhotographerException(PhotographerErrorCode.SPECIALTY_REQUIRED);
        }
    }
}
