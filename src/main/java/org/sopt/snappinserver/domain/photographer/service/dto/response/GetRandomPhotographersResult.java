package org.sopt.snappinserver.domain.photographer.service.dto.response;

import java.util.List;
import org.sopt.snappinserver.domain.photographer.domain.entity.Photographer;
import org.sopt.snappinserver.domain.photographer.domain.entity.PhotographerSpecialty;
import org.sopt.snappinserver.global.enums.SnapCategory;

public record GetRandomPhotographersResult(
    Long id,
    String name,
    String profileImageUrl,
    boolean isNew,
    String bio,
    List<String> specialties
) {

    public static GetRandomPhotographersResult of(
        Photographer photographer,
        List<PhotographerSpecialty> specialties
    ) {
        return new GetRandomPhotographersResult(
            photographer.getId(),
            photographer.getNickname(),
            photographer.getUser().getProfileImageUrl(),
            photographer.isNewPhotographer(),
            photographer.getBio(),
            specialties.stream()
                .map(PhotographerSpecialty::getSpecialty)
                .map(SnapCategory::getCategory)
                .toList()
        );
    }
}
