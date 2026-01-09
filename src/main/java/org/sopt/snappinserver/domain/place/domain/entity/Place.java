package org.sopt.snappinserver.domain.place.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
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
public class Place extends BaseEntity {

    private static final int MAX_NAME_LENGTH = 50;
    private static final int MAX_ADDRESS_LENGTH = 255;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "place_seq_gen")
    @SequenceGenerator(
        name = "place_seq_gen",
        sequenceName = "place_seq",
        allocationSize = 1
    )
    private Long id;

    @Column(nullable = false, length = MAX_NAME_LENGTH)
    private String name;

    @Column(nullable = false)
    private String address;

    @Builder(access = AccessLevel.PRIVATE)
    private Place(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public static Place create(String name, String address) {
        validatePlace(name, address);
        return Place.builder()
            .name(name)
            .address(address)
            .build();
    }

    private static void validatePlace(String name, String address) {
        validateName(name);
        validateAddress(address);
    }

    private static void validateName(String name) {
        validateNameExists(name);
        validateNameLength(name);
    }

    private static void validateNameExists(String name) {
        if (name == null || name.isBlank()) {
            throw new PlaceException(PlaceErrorCode.NAME_REQUIRED);
        }
    }

    private static void validateNameLength(String name) {
        if (name.length() > MAX_NAME_LENGTH) {
            throw new PlaceException(PlaceErrorCode.NAME_TOO_LONG);
        }
    }

    private static void validateAddress(String address) {
        validateAddressExists(address);
        validateAddressLength(address);
    }

    private static void validateAddressExists(String address) {
        if (address == null || address.isBlank()) {
            throw new PlaceException(PlaceErrorCode.ADDRESS_REQUIRED);
        }
    }

    private static void validateAddressLength(String address) {
        if (address.length() > MAX_ADDRESS_LENGTH) {
            throw new PlaceException(PlaceErrorCode.ADDRESS_TOO_LONG);
        }
    }

}
