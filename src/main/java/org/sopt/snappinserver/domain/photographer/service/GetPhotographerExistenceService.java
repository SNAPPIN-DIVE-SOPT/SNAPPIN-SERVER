package org.sopt.snappinserver.domain.photographer.service;

import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.photographer.repository.PhotographerRepository;
import org.sopt.snappinserver.domain.photographer.service.dto.response.GetPhotographerExistenceResult;
import org.sopt.snappinserver.domain.photographer.service.usecase.GetPhotographerExistenceUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class GetPhotographerExistenceService implements GetPhotographerExistenceUseCase {

    private final PhotographerRepository photographerRepository;

    public GetPhotographerExistenceResult getHasPhotographerProfile(Long userId) {
        return new GetPhotographerExistenceResult(photographerRepository.existsByUserId(userId));
    }
}
