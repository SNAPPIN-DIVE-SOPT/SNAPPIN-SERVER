package org.sopt.snappinserver.domain.place.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.snappinserver.domain.place.domain.exception.PlaceErrorCode;
import org.sopt.snappinserver.domain.place.domain.exception.PlaceException;
import org.sopt.snappinserver.global.entity.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(
        name = "uk_available_location_sido_sigungu",
        columnNames = {"sido", "sigungu"}
    )
})
public class AvailableLocation extends BaseEntity {

    private static final int MAX_SIDO_LENGTH = 10;
    private static final int MAX_SIGUNGU_LENGTH = 50;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "sido", nullable = false, length = MAX_SIDO_LENGTH)
    private String sido;

    @Column(name = "sigungu", length = MAX_SIGUNGU_LENGTH)
    private String sigungu;

    @Builder(access = AccessLevel.PRIVATE)
    private AvailableLocation(String sido, String sigungu) {
        this.sido = sido;
        this.sigungu = sigungu;
    }

    public static AvailableLocation create(String sido, String sigungu) {
        validateAvailableLocation(sido, sigungu);
        return AvailableLocation.builder()
            .sido(sido)
            .sigungu(sigungu)
            .build();
    }

    private static void validateAvailableLocation(String sido, String sigungu) {
        validateSido(sido);
        validateSigunguLength(sigungu);
    }

    private static void validateSido(String sido) {
        validateSidoExists(sido);
        validateSidoLength(sido);
    }

    private static void validateSidoExists(String sido) {
        if (sido == null || sido.isBlank()) {
            throw new PlaceException(PlaceErrorCode.SIDO_REQUIRED);
        }
    }

    private static void validateSidoLength(String sido) {
        if (sido.length() > MAX_SIDO_LENGTH) {
            throw new PlaceException(PlaceErrorCode.SIDO_TOO_LONG);
        }
    }

    private static void validateSigunguLength(String sigungu) {
        if (sigungu != null && sigungu.length() > MAX_SIGUNGU_LENGTH) {
            throw new PlaceException(PlaceErrorCode.SIGUNGU_TOO_LONG);
        }
    }
}
