package org.sopt.snappinserver.domain.photographer.service.dto.usecase;

import org.sopt.snappinserver.domain.photographer.service.dto.response.GetPhotographerProfileResult;

public interface GetPhotographerProfileUseCase {

    GetPhotographerProfileResult findPhotographerProfile(Long photographerId);
}
