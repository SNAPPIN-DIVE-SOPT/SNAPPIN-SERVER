package org.sopt.snappinserver.domain.reservation.domain.entity;

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
import org.sopt.snappinserver.domain.reservation.domain.exception.ReservationErrorCode;
import org.sopt.snappinserver.domain.reservation.domain.exception.ReservationException;
import org.sopt.snappinserver.global.entity.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ReservationAdditionalPayment extends BaseEntity {

    private static final int MAX_NAME_LENGTH = 100;
    private static final int MIN_AMOUNT_VALUE = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @Column(nullable = false, length = MAX_NAME_LENGTH)
    private String name;

    @Column(nullable = false)
    private Integer amount;

    @Builder(access = AccessLevel.PRIVATE)
    private ReservationAdditionalPayment(Reservation reservation, String name, Integer amount) {
        this.reservation = reservation;
        this.name = name;
        this.amount = amount;
    }

    public static ReservationAdditionalPayment create(
        Reservation reservation,
        String name,
        Integer amount
    ) {
        validateReservationAdditionalPayment(reservation, name, amount);
        return ReservationAdditionalPayment.builder()
            .reservation(reservation)
            .name(name)
            .amount(amount)
            .build();
    }

    private static void validateReservationAdditionalPayment(
        Reservation reservation,
        String name,
        Integer amount
    ) {
        validateReservationExists(reservation);
        validateName(name);
        validateAmount(amount);
    }

    private static void validateReservationExists(Reservation reservation) {
        if (reservation == null) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_REQUIRED);
        }
    }

    private static void validateName(String name) {
        validateNameExists(name);
        validateNameLength(name);
    }

    private static void validateNameExists(String name) {
        if (name == null || name.isBlank()) {
            throw new ReservationException(ReservationErrorCode.ADDITIONAL_PAYMENT_NAME_REQUIRED);
        }
    }

    private static void validateNameLength(String name) {
        if (name.length() > MAX_NAME_LENGTH) {
            throw new ReservationException(ReservationErrorCode.ADDITIONAL_PAYMENT_NAME_TOO_LONG);
        }
    }

    private static void validateAmount(Integer amount) {
        validateAmountExists(amount);
        validateMinAmount(amount);
    }

    private static void validateAmountExists(Integer amount) {
        if (amount == null) {
            throw new ReservationException(ReservationErrorCode.ADDITIONAL_PAYMENT_AMOUNT_REQUIRED);
        }
    }

    private static void validateMinAmount(Integer amount) {
        if (amount < MIN_AMOUNT_VALUE) {
            throw new ReservationException(
                ReservationErrorCode.ADDITIONAL_PAYMENT_AMOUNT_TOO_SMALL
            );
        }
    }

}
