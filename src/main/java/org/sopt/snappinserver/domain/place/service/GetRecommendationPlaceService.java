package org.sopt.snappinserver.domain.place.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.place.domain.entity.Place;
import org.sopt.snappinserver.domain.place.service.dto.response.GetRecommendationPlaceResult;
import org.sopt.snappinserver.domain.place.service.usecase.GetRecommendationPlaceUseCase;
import org.sopt.snappinserver.domain.portfolio.repository.PortfolioRepositoryCustom;
import org.sopt.snappinserver.domain.reservation.repository.ReservationRepositoryCustom;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class GetRecommendationPlaceService implements GetRecommendationPlaceUseCase {

    private final ReservationRepositoryCustom reservationRepositoryCustom;
    private final PortfolioRepositoryCustom portfolioRepositoryCustom;

    @Value("${cloud.aws.cloud-front.domain}")
    private String cloudFrontDomain;

    @Override
    public List<GetRecommendationPlaceResult> getPlaceRecommendation() {
        List<Place> places = reservationRepositoryCustom.findTop5MostReservedPlacesInLastMonth();

        return places.stream()
            .map(place -> {
                String imageKey = portfolioRepositoryCustom
                    .findBestPortfolioImageByPlaceId(place.getId())
                    .orElse(null);

                String presignedUrl = (imageKey != null) ? cloudFrontDomain + imageKey : null;

                return GetRecommendationPlaceResult.of(place, presignedUrl);
            })
            .toList();
    }
}
