package org.sopt.snappinserver.domain.place.service.dto.response;

import org.sopt.snappinserver.domain.place.domain.entity.Place;

public record GetPlaceResult(
    Long id,
    String name
) {

    public static GetPlaceResult from(Place place) {
        return new GetPlaceResult(place.getId(), place.getName());
    }
}
