package org.sopt.snappinserver.domain.photographer.service.usecase;

import org.sopt.snappinserver.domain.photographer.service.dto.response.GetPhotographerProfileResult;

public interface GetPhotographerProfileUseCase {

    GetPhotographerProfileResult getPhotographerProfile(Long photographerId);
}
