package org.sopt.snappinserver.domain.reservation.domain.entity;

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
import java.time.OffsetDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.snappinserver.domain.place.domain.entity.Place;
import org.sopt.snappinserver.domain.product.domain.entity.Product;
import org.sopt.snappinserver.domain.reservation.domain.enums.ReservationStatus;
import org.sopt.snappinserver.domain.reservation.domain.exception.ReservationErrorCode;
import org.sopt.snappinserver.domain.reservation.domain.exception.ReservationException;
import org.sopt.snappinserver.domain.user.domain.entity.User;
import org.sopt.snappinserver.global.entity.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Reservation extends BaseEntity {

    private static final int MAX_REQUEST_NOTE_LENGTH = 512;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @Column(nullable = false)
    private OffsetDateTime reservedAt;

    @Column(nullable = false)
    private Integer durationTime;

    @Column(nullable = false)
    private Integer peopleCount;

    @Column(length = MAX_REQUEST_NOTE_LENGTH)
    private String requestNote;

    private Integer extraPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus reservationStatus;

    @Builder(access = AccessLevel.PRIVATE)
    private Reservation(
        Product product,
        User user,
        Place place,
        OffsetDateTime reservedAt,
        Integer durationTime,
        Integer peopleCount,
        String requestNote,
        Integer extraPrice,
        ReservationStatus reservationStatus
    ) {
        this.product = product;
        this.user = user;
        this.place = place;
        this.reservedAt = reservedAt;
        this.durationTime = durationTime;
        this.peopleCount = peopleCount;
        this.requestNote = requestNote;
        this.extraPrice = extraPrice;
        this.reservationStatus = reservationStatus;
    }

    public static Reservation create(
        Product product,
        User user,
        Place place,
        OffsetDateTime reservedAt,
        Integer durationTime,
        Integer peopleCount,
        String requestNote,
        Integer extraPrice,
        ReservationStatus reservationStatus
    ) {
        validateReservation(
            product, user, place, reservedAt, durationTime, peopleCount, requestNote,
            reservationStatus
        );
        return Reservation.builder()
            .product(product)
            .user(user)
            .place(place)
            .reservedAt(reservedAt)
            .durationTime(durationTime)
            .peopleCount(peopleCount)
            .requestNote(requestNote)
            .extraPrice(extraPrice)
            .reservationStatus(reservationStatus)
            .build();
    }

    private static void validateReservation(
        Product product,
        User user,
        Place place,
        OffsetDateTime reservedAt,
        Integer durationTime,
        Integer peopleCount,
        String requestNote,
        ReservationStatus reservationStatus
    ) {
        validateProductExists(product);
        validateUserExists(user);
        validatePlaceExists(place);
        validateReservedAtExists(reservedAt);
        validateDurationTimeExists(durationTime);
        validatePeopleCountExists(peopleCount);
        validateRequestNoteLength(requestNote);
        validateReservationStatusExists(reservationStatus);
    }

    private static void validateProductExists(Product product) {
        if (product == null) {
            throw new ReservationException(ReservationErrorCode.PRODUCT_REQUIRED);
        }
    }

    private static void validateUserExists(User user) {
        if (user == null) {
            throw new ReservationException(ReservationErrorCode.USER_REQUIRED);
        }
    }

    private static void validatePlaceExists(Place place) {
        if (place == null) {
            throw new ReservationException(ReservationErrorCode.PLACE_REQUIRED);
        }
    }

    private static void validateReservedAtExists(OffsetDateTime reservedAt) {
        if (reservedAt == null) {
            throw new ReservationException(ReservationErrorCode.RESERVED_AT_REQUIRED);
        }
    }

    private static void validateDurationTimeExists(Integer durationTime) {
        if (durationTime == null) {
            throw new ReservationException(ReservationErrorCode.DURATION_TIME_REQUIRED);
        }
    }

    private static void validatePeopleCountExists(Integer peopleCount) {
        if (peopleCount == null) {
            throw new ReservationException(ReservationErrorCode.PEOPLE_COUNT_REQUIRED);
        }
    }

    private static void validateRequestNoteLength(String requestNote) {
        if (requestNote != null && requestNote.length() > MAX_REQUEST_NOTE_LENGTH) {
            throw new ReservationException(ReservationErrorCode.REQUEST_NOTE_TOO_LONG);
        }
    }

    private static void validateReservationStatusExists(ReservationStatus reservationStatus) {
        if (reservationStatus == null) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_STATUS_REQUIRED);
        }
    }
}
