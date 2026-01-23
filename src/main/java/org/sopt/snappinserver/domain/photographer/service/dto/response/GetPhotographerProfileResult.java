package org.sopt.snappinserver.domain.photographer.service.dto.response;

import java.util.List;
import org.sopt.snappinserver.domain.photographer.domain.entity.Photographer;
import org.sopt.snappinserver.domain.photographer.domain.entity.PhotographerAvailableLocation;
import org.sopt.snappinserver.domain.photographer.domain.entity.PhotographerSpecialty;
import org.sopt.snappinserver.domain.place.domain.entity.AvailableLocation;
import org.sopt.snappinserver.global.enums.SnapCategory;

public record GetPhotographerProfileResult(
    Long id,
    String name,
    String profileImageUrl,
    String bio,
    List<String> specialties,
    List<String> locations
) {

    public static GetPhotographerProfileResult of(
        Photographer photographer,
        String profileImageUrl,
        List<PhotographerSpecialty> specialties,
        List<PhotographerAvailableLocation> locations
    ) {
        return new GetPhotographerProfileResult(
            photographer.getId(),
            photographer.getNickname(),
            profileImageUrl,
            photographer.getBio(),

            specialties.stream()
                .map(PhotographerSpecialty::getSpecialty)
                .map(SnapCategory::getCategory)
                .toList(),

            locations.stream()
                .map(PhotographerAvailableLocation::getAvailableLocation)
                .map(AvailableLocation::getFullLocation)
                .toList()
        );
    }
}
