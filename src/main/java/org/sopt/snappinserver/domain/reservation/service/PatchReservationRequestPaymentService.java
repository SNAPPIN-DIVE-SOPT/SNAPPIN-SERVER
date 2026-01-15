package org.sopt.snappinserver.domain.reservation.service;

import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.reservation.domain.entity.Reservation;
import org.sopt.snappinserver.domain.reservation.domain.entity.ReservationAdditionalPayment;
import org.sopt.snappinserver.domain.reservation.domain.exception.ReservationErrorCode;
import org.sopt.snappinserver.domain.reservation.domain.exception.ReservationException;
import org.sopt.snappinserver.domain.reservation.repository.ReservationAdditionalPaymentRepository;
import org.sopt.snappinserver.domain.reservation.repository.ReservationRepository;
import org.sopt.snappinserver.domain.reservation.service.dto.request.ExtraPriceCommand;
import org.sopt.snappinserver.domain.reservation.service.dto.request.RequestPaymentReservationCommand;
import org.sopt.snappinserver.domain.reservation.service.dto.response.RequestPaymentReservationResult;
import org.sopt.snappinserver.domain.reservation.service.usecase.PatchReservationRequestPaymentUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PatchReservationRequestPaymentService implements
    PatchReservationRequestPaymentUseCase {

    private final ReservationRepository reservationRepository;
    private final ReservationAdditionalPaymentRepository additionalPaymentRepository;


    @Override
    public RequestPaymentReservationResult requestPaymentReservation(
        RequestPaymentReservationCommand command
    ) {
        Reservation reservation = getReservation(command);

        validateReservationPhotographer(command, reservation);
        validateBasePrice(command, reservation);
        int extraTotal = saveExtraPrice(command, reservation);
        validateTotalPrice(command, extraTotal);

        reservation.requestPayment();

        return new RequestPaymentReservationResult(
            reservation.getId(),
            reservation.getReservationStatus(),
            command.basePrice(),
            extraTotal,
            command.totalPrice()
        );
    }

    private Reservation getReservation(RequestPaymentReservationCommand command) {
        return reservationRepository.findById(command.reservationId())
            .orElseThrow(() -> new ReservationException(ReservationErrorCode.RESERVATION_NOT_FOUND));
    }

    private void validateReservationPhotographer(
        RequestPaymentReservationCommand command,
        Reservation reservation
    ) {
        if (!reservation.isReservationPhotographer(command.photographerUserId())) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_USER_NOT_MATCH);
        }
    }

    private void validateBasePrice(
        RequestPaymentReservationCommand command,
        Reservation reservation
    ) {
        int productPrice = reservation.getProduct().getPrice();
        if (productPrice != command.basePrice()) {
            throw new ReservationException(ReservationErrorCode.INVALID_BASE_PRICE);
        }
    }

    private int saveExtraPrice(RequestPaymentReservationCommand command, Reservation reservation) {
        int extraTotal = 0;
        for (ExtraPriceCommand extra
            : command.extraPrices()) {

            ReservationAdditionalPayment payment = ReservationAdditionalPayment.create(
                reservation,
                extra.name(),
                extra.amount()
            );

            additionalPaymentRepository.save(payment);
            extraTotal += extra.amount();
        }
        return extraTotal;
    }

    private void validateTotalPrice(
        RequestPaymentReservationCommand command,
        int extraTotal
    ) {
        if (command.basePrice() + extraTotal != command.totalPrice()) {
            throw new ReservationException(ReservationErrorCode.INVALID_TOTAL_PRICE);
        }
    }
}
