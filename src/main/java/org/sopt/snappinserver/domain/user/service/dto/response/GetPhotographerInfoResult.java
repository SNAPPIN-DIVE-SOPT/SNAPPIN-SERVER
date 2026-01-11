package org.sopt.snappinserver.domain.user.service.dto.response;

import java.util.List;

public record GetPhotographerInfoResult(
    String name,
    String bio,
    List<String> specialties,
    List<String> locations
) {

}
