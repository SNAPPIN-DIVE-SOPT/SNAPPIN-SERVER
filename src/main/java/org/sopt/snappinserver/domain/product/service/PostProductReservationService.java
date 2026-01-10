package org.sopt.snappinserver.domain.product.service;

import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.place.domain.entity.Place;
import org.sopt.snappinserver.domain.place.domain.exception.PlaceErrorCode;
import org.sopt.snappinserver.domain.place.domain.exception.PlaceException;
import org.sopt.snappinserver.domain.place.repository.PlaceRepository;
import org.sopt.snappinserver.domain.product.domain.entity.Product;
import org.sopt.snappinserver.domain.product.domain.exception.ProductErrorCode;
import org.sopt.snappinserver.domain.product.domain.exception.ProductException;
import org.sopt.snappinserver.domain.product.repository.ProductRepository;
import org.sopt.snappinserver.domain.product.service.dto.request.ProductReservationCommand;
import org.sopt.snappinserver.domain.product.service.dto.response.ProductReservationResult;
import org.sopt.snappinserver.domain.product.service.usecase.PostProductReservationUseCase;
import org.sopt.snappinserver.domain.reservation.domain.entity.Reservation;
import org.sopt.snappinserver.domain.reservation.domain.enums.ReservationStatus;
import org.sopt.snappinserver.domain.reservation.repository.ReservationRepository;
import org.sopt.snappinserver.domain.user.domain.entity.User;
import org.sopt.snappinserver.domain.user.domain.exception.UserErrorCode;
import org.sopt.snappinserver.domain.user.domain.exception.UserException;
import org.sopt.snappinserver.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class PostProductReservationService implements PostProductReservationUseCase {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private final ReservationRepository reservationRepository;

    @Override
    public ProductReservationResult reserve(
        Long productId,
        Long userId,
        ProductReservationCommand command
    ) {
        Product product = getProduct(productId);
        User user = getUser(userId);
        Place place = getPlace(command);

        Reservation reservation = createReservation(product, user, place, command);
        Reservation saved = reservationRepository.save(reservation);

        return new ProductReservationResult(saved.getId(), saved.getReservationStatus());
    }

    private Product getProduct(Long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    }

    private Place getPlace(ProductReservationCommand command) {
        return placeRepository.findById(command.placeId())
            .orElseThrow(() -> new PlaceException(PlaceErrorCode.PLACE_NOT_FOUND));
    }

    private Reservation createReservation(
        Product product,
        User user,
        Place place,
        ProductReservationCommand command
    ) {
        return Reservation.create(
            product,
            user,
            place,
            command.reservedAt(),
            command.durationTime(),
            command.peopleCount(),
            command.requestNote(),
            ReservationStatus.RESERVATION_REQUESTED,
            null);
    }
}
