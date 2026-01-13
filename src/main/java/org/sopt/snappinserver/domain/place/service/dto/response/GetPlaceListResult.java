package org.sopt.snappinserver.domain.place.service.dto.response;

import java.util.List;
import org.sopt.snappinserver.domain.place.domain.entity.Place;

public record GetPlaceListResult(
    List<GetPlaceResult> places
) {

    public static GetPlaceListResult from(List<Place> results) {
        return new GetPlaceListResult(
            results.stream()
                .map(GetPlaceResult::from)
                .toList()
        );
    }
}
