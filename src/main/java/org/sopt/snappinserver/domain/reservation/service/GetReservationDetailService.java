package org.sopt.snappinserver.domain.reservation.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.product.domain.entity.Product;
import org.sopt.snappinserver.domain.product.repository.ProductMoodRepository;
import org.sopt.snappinserver.domain.product.repository.ProductPhotoRepository;
import org.sopt.snappinserver.domain.product.service.dto.response.ProductReviewStatsResult;
import org.sopt.snappinserver.domain.reservation.domain.entity.Reservation;
import org.sopt.snappinserver.domain.reservation.domain.entity.ReservationAdditionalPayment;
import org.sopt.snappinserver.domain.reservation.domain.exception.ReservationErrorCode;
import org.sopt.snappinserver.domain.reservation.domain.exception.ReservationException;
import org.sopt.snappinserver.domain.reservation.repository.ReservationAdditionalPaymentRepository;
import org.sopt.snappinserver.domain.reservation.repository.ReservationRepository;
import org.sopt.snappinserver.domain.reservation.service.dto.response.GetReservationDetailInfoResult;
import org.sopt.snappinserver.domain.reservation.service.dto.response.GetReservationDetailPaymentResult;
import org.sopt.snappinserver.domain.reservation.service.dto.response.GetReservationDetailProductResult;
import org.sopt.snappinserver.domain.reservation.service.dto.response.GetReservationDetailResult;
import org.sopt.snappinserver.domain.reservation.service.dto.response.GetReservationDetailReviewResult;
import org.sopt.snappinserver.domain.reservation.service.usecase.GetReservationDetailUseCase;
import org.sopt.snappinserver.domain.review.domain.entity.ReviewPhoto;
import org.sopt.snappinserver.domain.review.repository.ReviewPhotoRepository;
import org.sopt.snappinserver.domain.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetReservationDetailService implements GetReservationDetailUseCase {

    private static final ZoneId KOREA_ZONE = ZoneId.of("Asia/Seoul");
    private final ReservationRepository reservationRepository;
    private final ProductPhotoRepository productPhotoRepository;
    private final ProductMoodRepository productMoodRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewPhotoRepository reviewPhotoRepository;
    private final ReservationAdditionalPaymentRepository reservationAdditionalPaymentRepository;

    @Override
    public GetReservationDetailResult getReservationDetail(Long userId, Long reservationId) {
        Reservation reservation = getReservation(reservationId, userId);
        Product product = reservation.getProduct();

        GetReservationDetailProductResult productResult = mapToProductResult(product);
        GetReservationDetailInfoResult infoResult = mapToReservationInfoResult(reservation);
        GetReservationDetailPaymentResult paymentResult = mapToPaymentResult(reservation, product);
        GetReservationDetailReviewResult reviewResult = mapToReviewResult(reservation);

        return new GetReservationDetailResult(
            reservation.getReservationStatus(),
            productResult,
            infoResult,
            paymentResult,
            reviewResult
        );
    }

    private Reservation getReservation(Long reservationId, Long userId) {
        Reservation reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new ReservationException(ReservationErrorCode.RESERVATION_NOT_FOUND));

        if (!reservation.validateReservationClient(userId)) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_USER_NOT_MATCH);
        }

        return reservation;
    }

    private String getThumbnail(Product product) {
        return productPhotoRepository.findFirstByProductOrderByDisplayOrderAsc(product)
            .map(pp -> pp.getPhoto().getImageUrl())
            .orElse(null);
    }

    private ProductReviewStatsResult getReviewStats(Product product) {
        return reviewRepository.findReviewStatsByProductId(product.getId());
    }

    private GetReservationDetailProductResult mapToProductResult(Product product) {
        ProductReviewStatsResult stats = getReviewStats(product);

        return new GetReservationDetailProductResult(
            product.getId(),
            getThumbnail(product),
            product.getTitle(),
            stats != null ? stats.averageRating() : 0.0,
            stats != null ? Math.toIntExact(stats.reviewCount()) : 0,
            product.getPhotographer().getName(),
            product.getPrice(),
            getMoodNames(product)
        );
    }

    private List<String> getMoodNames(Product product) {
        return productMoodRepository.findAllByProduct(product).stream()
            .map(pm -> pm.getMood().getName())
            .toList();
    }

    private GetReservationDetailInfoResult mapToReservationInfoResult(Reservation reservation) {
        return new GetReservationDetailInfoResult(
            reservation.getUser().getName(),
            LocalDateTime.ofInstant(reservation.getCreatedAt(), KOREA_ZONE),
            reservation.getReservedAt().toLocalDate(),
            reservation.getReservedAt().toLocalTime(),
            reservation.getDurationTime(),
            reservation.getPlace().getName(),
            reservation.getPeopleCount(),
            reservation.getRequestNote()
        );
    }

    private GetReservationDetailPaymentResult mapToPaymentResult(
        Reservation reservation,
        Product product
    ) {
        List<ReservationAdditionalPayment> additionalPayments =
            reservationAdditionalPaymentRepository.findAllByReservation(reservation);

        int extraPrice = additionalPayments.stream()
            .mapToInt(ReservationAdditionalPayment::getAmount)
            .sum();

        int basePrice = product.getPrice();

        return new GetReservationDetailPaymentResult(
            basePrice,
            extraPrice,
            basePrice + extraPrice
        );
    }

    private GetReservationDetailReviewResult mapToReviewResult(Reservation reservation) {
        return reviewRepository.findByReservation(reservation)
            .map(review -> {
                List<ReviewPhoto> photos =
                    reviewPhotoRepository.findAllByReviewIds(List.of(review.getId()));

                return new GetReservationDetailReviewResult(
                    review.getId(),
                    reservation.getUser().getName(),
                    review.getRating(),
                    LocalDate.ofInstant(review.getCreatedAt(), KOREA_ZONE),
                    photos.stream().map(rp -> rp.getPhoto().getImageUrl()).toList(),
                    review.getContent()
                );
            })
            .orElse(null);
    }
}
