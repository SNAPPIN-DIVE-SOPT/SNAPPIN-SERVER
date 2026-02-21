package org.sopt.snappinserver.domain.photographer.service.usecase;

import org.sopt.snappinserver.domain.photographer.service.dto.response.GetPhotographerExistenceResult;

public interface GetPhotographerExistenceUseCase {

    GetPhotographerExistenceResult getHasPhotographerProfile(Long userId);
}
