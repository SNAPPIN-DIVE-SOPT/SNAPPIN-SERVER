package org.sopt.snappinserver.domain.portfolio.service.dto.response;

import java.util.List;
import org.sopt.snappinserver.domain.photographer.domain.entity.Photographer;

public record GetPhotographerInfoResult(
    Long id,
    String name,
    String bio,
    List<String> specialties,
    List<String> locations
) {

    public static GetPhotographerInfoResult of(
        Photographer photographer,
        List<String> photographerSpecialties,
        List<String> photographerAvailableLocations
    ) {
        return new GetPhotographerInfoResult(
            photographer.getId(),
            photographer.getNickname(),
            photographer.getBio(),
            photographerSpecialties,
            photographerAvailableLocations
        );
    }
}
