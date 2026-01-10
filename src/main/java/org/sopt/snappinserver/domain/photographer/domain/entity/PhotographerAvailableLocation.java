package org.sopt.snappinserver.domain.photographer.domain.entity;

import jakarta.persistence.Entity;
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
import org.sopt.snappinserver.domain.place.domain.entity.AvailableLocation;
import org.sopt.snappinserver.global.entity.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    name = "photographer_available_location",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_photographer_available_location",
            columnNames = {"photographer_id", "available_location_id"}
        )
    }
)
public class PhotographerAvailableLocation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "photographer_available_location_seq_gen")
    @SequenceGenerator(
        name = "photographer_available_location_seq_gen",
        sequenceName = "photographer_available_location_specialty_seq",
        allocationSize = 1
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photographer_id", nullable = false)
    private Photographer photographer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "available_location_id", nullable = false)
    private AvailableLocation availableLocation;

    @Builder(access = AccessLevel.PRIVATE)
    private PhotographerAvailableLocation(
        Photographer photographer,
        AvailableLocation availableLocation
    ) {
        this.photographer = photographer;
        this.availableLocation = availableLocation;
    }

    public static PhotographerAvailableLocation create(
        Photographer photographer,
        AvailableLocation availableLocation
    ) {
        validatePhotographerAvailableLocation(photographer, availableLocation);

        return PhotographerAvailableLocation.builder()
            .photographer(photographer)
            .availableLocation(availableLocation)
            .build();
    }

    private static void validatePhotographerAvailableLocation(
        Photographer photographer,
        AvailableLocation availableLocation
    ) {
        validatePhotographerExists(photographer);
        validateAvailableLocationExists(availableLocation);
    }

    private static void validatePhotographerExists(Photographer photographer) {
        if (photographer == null) {
            throw new PhotographerException(PhotographerErrorCode.PHOTOGRAPHER_REQUIRED);
        }
    }

    private static void validateAvailableLocationExists(AvailableLocation availableLocation) {
        if (availableLocation == null) {
            throw new PhotographerException(PhotographerErrorCode.AVAILABLE_LOCATION_REQUIRED);
        }
    }

}
