package org.sopt.snappinserver.domain.place.service.dto.response;

import org.sopt.snappinserver.domain.place.domain.entity.Place;

public record GetRecommendationPlaceResult(
    Long id,
    String name,
    String imageUrl
) {

    public static GetRecommendationPlaceResult of(Place place, String imageUrl) {
        return new GetRecommendationPlaceResult(
            place.getId(),
            place.getName(),
            imageUrl
        );
    }
}
