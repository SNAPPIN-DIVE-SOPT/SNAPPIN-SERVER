package org.sopt.snappinserver.domain.product.service.dto.response;

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
        List<String> specialties,
        List<String> locations
    ) {
        return new GetPhotographerInfoResult(
            photographer.getId(),
            photographer.getNickname(),
            photographer.getBio(),
            specialties,
            locations
        );
    }
}
