package org.sopt.snappinserver.domain.reservation.service;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.product.repository.ProductMoodRepository;
import org.sopt.snappinserver.domain.product.repository.ProductPhotoRepository;
import org.sopt.snappinserver.domain.product.service.dto.response.ProductReviewStatsResult;
import org.sopt.snappinserver.domain.reservation.domain.entity.Reservation;
import org.sopt.snappinserver.domain.reservation.domain.enums.ReservationStatus;
import org.sopt.snappinserver.domain.reservation.domain.enums.ReservationStatusTab;
import org.sopt.snappinserver.domain.reservation.repository.ReservationRepository;
import org.sopt.snappinserver.domain.reservation.service.dto.response.GetReservationListItemResult;
import org.sopt.snappinserver.domain.reservation.service.dto.response.GetReservationListProductResult;
import org.sopt.snappinserver.domain.reservation.service.dto.response.GetReservationListResult;
import org.sopt.snappinserver.domain.reservation.service.usecase.GetReservationListUseCase;
import org.sopt.snappinserver.domain.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetReservationListService implements GetReservationListUseCase {

    private static final DateTimeFormatter FORMATTER =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final ReservationRepository reservationRepository;
    private final ReviewRepository reviewRepository;
    private final ProductMoodRepository productMoodRepository;
    private final ProductPhotoRepository productPhotoRepository;

    @Override
    public GetReservationListResult getReservationList(
        Long userId,
        ReservationStatusTab tab
    ) {
        // 1. status 기준 조회
        List<Reservation> reservations =
            tab.isClientTab()
                ? reservationRepository.findClientReservations(
                userId, tab.getRelatedStatus()
            )
                : reservationRepository.findPhotographerReservations(
                    userId, tab.getRelatedStatus()
                );

        // 2. 취소/거절 이전 상태 필터
        List<Reservation> filtered = reservations.stream()
            .filter(r -> isAllowedCanceledReservation(r, tab))
            .toList();

        if (filtered.isEmpty()) {
            return new GetReservationListResult(List.of());
        }

        // 3. ID 수집
        List<Long> reservationIds =
            filtered.stream().map(Reservation::getId).toList();

        List<Long> productIds =
            filtered.stream()
                .map(r -> r.getProduct().getId())
                .distinct()
                .toList();

        // 4. 리뷰 작성 여부
        Set<Long> reviewedReservationIds =
            new HashSet<>(reviewRepository.findReviewedReservationIds(reservationIds));

        // 5. 상품 리뷰 통계 (batch)
        Map<Long, ProductReviewStatsResult> reviewStatsMap =
            reviewRepository.findReviewStatsByProductIds(productIds).stream()
                .collect(Collectors.toMap(
                    row -> (Long) row[0],
                    row -> (ProductReviewStatsResult) row[1]
                ));

        // 6. 상품 무드
        Map<Long, List<String>> productMoodMap =
            productMoodRepository.findAllByProductIdIn(productIds).stream()
                .collect(Collectors.groupingBy(
                    pm -> pm.getProduct().getId(),
                    Collectors.mapping(
                        pm -> pm.getMood().getName(),
                        Collectors.toList()
                    )
                ));

        // 7. 썸네일
        Map<Long, String> productThumbnailMap =
            productPhotoRepository.findThumbnailByProductIds(productIds);

        // 8. DTO 변환
        List<GetReservationListItemResult> results =
            filtered.stream()
                .map(r -> toItemResult(
                    r,
                    productThumbnailMap,
                    productMoodMap,
                    reviewedReservationIds,
                    reviewStatsMap
                ))
                .toList();

        return new GetReservationListResult(results);
    }

    private boolean isAllowedCanceledReservation(
        Reservation reservation,
        ReservationStatusTab tab
    ) {
        ReservationStatus status = reservation.getReservationStatus();

        if (status != ReservationStatus.RESERVATION_CANCELED
            && status != ReservationStatus.RESERVATION_REFUSED) {
            return true;
        }

        ReservationStatus prev = reservation.getPreviousCancelStatus();

        return switch (tab) {
            case PHOTOGRAPHER_REQUESTED -> prev == ReservationStatus.RESERVATION_REQUESTED;

            case PHOTOGRAPHER_ADJUSTING -> prev == ReservationStatus.PHOTOGRAPHER_CHECKING
                || prev == ReservationStatus.PAYMENT_REQUESTED
                || prev == ReservationStatus.PAYMENT_COMPLETED;

            case PHOTOGRAPHER_CONFIRMED -> prev == ReservationStatus.RESERVATION_CONFIRMED;

            case CLIENT_OVERVIEW -> true;

            default -> false;
        };
    }

    private GetReservationListItemResult toItemResult(
        Reservation reservation,
        Map<Long, String> productThumbnailMap,
        Map<Long, List<String>> productMoodMap,
        Set<Long> reviewedReservationIds,
        Map<Long, ProductReviewStatsResult> reviewStatsMap
    ) {
        var product = reservation.getProduct();
        Long productId = product.getId();

        ProductReviewStatsResult stats =
            reviewStatsMap.getOrDefault(
                productId,
                new ProductReviewStatsResult(0L, 0.0)
            );

        return new GetReservationListItemResult(
            reservation.getId(),
            reservation.getReservationStatus().name(),
            reservation.getUser().getName(),
            reservation.getCreatedAt()
                .atZone(ZoneId.systemDefault())
                .format(FORMATTER),
            new GetReservationListProductResult(
                productId,
                productThumbnailMap.get(productId),
                product.getTitle(),
                stats.averageRating(),
                (int) stats.reviewCount(),
                product.getPhotographer().getUser().getName(),
                product.getPrice(),
                productMoodMap.getOrDefault(productId, List.of()),
                reviewedReservationIds.contains(reservation.getId())
            )
        );
    }
}
