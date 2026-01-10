package org.sopt.snappinserver.domain.photographer.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.photographer.domain.entity.Photographer;
import org.sopt.snappinserver.domain.photographer.domain.entity.PhotographerAvailableLocation;
import org.sopt.snappinserver.domain.photographer.domain.entity.PhotographerSpecialty;
import org.sopt.snappinserver.domain.photographer.domain.exception.PhotographerErrorCode;
import org.sopt.snappinserver.domain.photographer.domain.exception.PhotographerException;
import org.sopt.snappinserver.domain.photographer.repository.PhotographerAvailableLocationRepository;
import org.sopt.snappinserver.domain.photographer.repository.PhotographerRepository;
import org.sopt.snappinserver.domain.photographer.repository.PhotographerSpecialtyRepository;
import org.sopt.snappinserver.domain.photographer.service.dto.response.GetPhotographerProfileResult;
import org.sopt.snappinserver.domain.photographer.service.usecase.GetPhotographerProfileUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class GetPhotographerProfileService implements GetPhotographerProfileUseCase {

    private final PhotographerRepository photographerRepository;
    private final PhotographerSpecialtyRepository photographerSpecialtyRepository;
    private final PhotographerAvailableLocationRepository photographerAvailableLocationRepository;

    public GetPhotographerProfileResult getPhotographerProfile(Long photographerId) {
        Photographer photographer = getExistingPhotographer(photographerId);

        List<PhotographerSpecialty> specialties = photographerSpecialtyRepository
            .findAllByPhotographer(photographer);
        validateSpecialtyExists(specialties);

        List<PhotographerAvailableLocation> availableLocations = photographerAvailableLocationRepository
            .findAllByPhotographer(photographer);
        validateAvailableLocationExists(availableLocations);

        return GetPhotographerProfileResult.of(photographer, specialties, availableLocations);
    }

    private Photographer getExistingPhotographer(Long photographerId) {
        return photographerRepository.findById(photographerId)
            .orElseThrow(
                () -> new PhotographerException(PhotographerErrorCode.PHOTOGRAPHER_NOT_FOUND)
            );
    }

    private void validateSpecialtyExists(List<PhotographerSpecialty> specialties) {
        if (specialties.isEmpty()) {
            throw new PhotographerException(PhotographerErrorCode.PHOTOGRAPHER_SPECIALTY_NOT_FOUND);
        }
    }

    private void validateAvailableLocationExists(
        List<PhotographerAvailableLocation> availableLocations
    ) {
        if (availableLocations.isEmpty()) {
            throw new PhotographerException(
                PhotographerErrorCode.PHOTOGRAPHER_AVAILABLE_LOCATION_NOT_FOUND
            );
        }
    }

}
