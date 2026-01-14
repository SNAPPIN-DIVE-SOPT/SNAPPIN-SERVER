package org.sopt.snappinserver.domain.place.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.place.domain.entity.Place;
import org.sopt.snappinserver.domain.place.service.dto.response.GetRecommendationPlaceResult;
import org.sopt.snappinserver.domain.place.service.usecase.GetRecommendationPlaceUseCase;
import org.sopt.snappinserver.domain.portfolio.repository.PortfolioRepositoryCustom;
import org.sopt.snappinserver.domain.reservation.repository.ReservationRepositoryCustom;
import org.sopt.snappinserver.global.s3.S3Service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class GetRecommendationPlaceService implements GetRecommendationPlaceUseCase {

    private final ReservationRepositoryCustom reservationRepositoryCustom;
    private final PortfolioRepositoryCustom portfolioRepositoryCustom;
    private final S3Service s3Service;

    @Override
    public List<GetRecommendationPlaceResult> getPlaceRecommendation() {
        List<Place> places = reservationRepositoryCustom.findTop5MostReservedPlacesInLastMonth();

        return places.stream()
            .map(place -> {
                String imageKey = portfolioRepositoryCustom
                    .findBestPortfolioImageByPlaceId(place.getId())
                    .orElse(null);

                String presignedUrl = (imageKey != null)
                    ? s3Service.getPresignedUrl(imageKey)
                    : null;

                return GetRecommendationPlaceResult.of(place, presignedUrl);
            })
            .toList();
    }
}
