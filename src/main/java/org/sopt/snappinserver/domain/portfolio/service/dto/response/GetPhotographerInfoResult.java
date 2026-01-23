package org.sopt.snappinserver.domain.portfolio.service.dto.response;

import java.util.List;
import org.sopt.snappinserver.domain.photographer.domain.entity.Photographer;

public record GetPhotographerInfoResult(
    Long id,
    String name,
    String imageUrl,
    String bio,
    List<String> specialties,
    List<String> locations
) {

    public static GetPhotographerInfoResult of(
        Photographer photographer,
        String profileImageUrl,
        List<String> photographerSpecialties,
        List<String> photographerAvailableLocations
    ) {
        return new GetPhotographerInfoResult(
            photographer.getId(),
            photographer.getNickname(),
            profileImageUrl,
            photographer.getBio(),
            photographerSpecialties,
            photographerAvailableLocations
        );
    }
}
