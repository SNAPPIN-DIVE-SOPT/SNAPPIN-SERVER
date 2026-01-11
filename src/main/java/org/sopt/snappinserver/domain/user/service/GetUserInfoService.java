package org.sopt.snappinserver.domain.user.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.curation.repository.CurationRepositoryCustom;
import org.sopt.snappinserver.domain.mood.domain.entity.Mood;
import org.sopt.snappinserver.domain.mood.repository.MoodRepository;
import org.sopt.snappinserver.domain.photographer.domain.entity.Photographer;
import org.sopt.snappinserver.domain.photographer.repository.PhotographerAvailableLocationRepository;
import org.sopt.snappinserver.domain.photographer.repository.PhotographerRepository;
import org.sopt.snappinserver.domain.photographer.repository.PhotographerSpecialtyRepository;
import org.sopt.snappinserver.domain.user.domain.entity.User;
import org.sopt.snappinserver.domain.user.domain.exception.UserErrorCode;
import org.sopt.snappinserver.domain.user.domain.exception.UserException;
import org.sopt.snappinserver.domain.user.repository.UserRepository;
import org.sopt.snappinserver.domain.user.service.dto.response.GetClientInfoResult;
import org.sopt.snappinserver.domain.user.service.dto.response.GetPhotographerInfoResult;
import org.sopt.snappinserver.domain.user.service.dto.response.GetUserInfoResult;
import org.sopt.snappinserver.domain.user.service.usecase.GetUserInfoUseCase;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GetUserInfoService implements GetUserInfoUseCase {

    private final UserRepository userRepository;
    private final PhotographerRepository photographerRepository;
    private final MoodRepository moodRepository;
    private final CurationRepositoryCustom curationRepository;
    private final PhotographerSpecialtyRepository specialtyRepository;
    private final PhotographerAvailableLocationRepository locationRepository;

    @Override
    public GetUserInfoResult getUserInfo(Long userId) {
        User user = getUser(userId);
        Optional<Photographer> photographerOpt = photographerRepository.findByUser(user);

        if (user.isLoginByClient()) {
            return getClientInfo(user, photographerOpt);
        }
        return getPhotographerInfo(user, photographerOpt);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    }

    private GetUserInfoResult getClientInfo(
        User user,
        Optional<Photographer> photographerOptional
    ) {
        List<Long> moodIds = curationRepository.findTop3MoodIdsByUserId(user.getId());
        List<String> moodNames = getMoodNames(moodIds);
        GetClientInfoResult clientInfo = new GetClientInfoResult(user.getName(), moodNames);

        return GetUserInfoResult.of(user, photographerOptional, clientInfo, null);
    }

    private List<String> getMoodNames(List<Long> moodIds) {
        return moodRepository.findAllById(moodIds)
            .stream()
            .map(Mood::getName)
            .toList();
    }


    private GetUserInfoResult getPhotographerInfo(
        User user,
        Optional<Photographer> photographerOptional
    ) {
        Photographer photographer = getPhotographer(photographerOptional);
        List<String> specialties = getSpecialties(photographer);
        List<String> locations = getAvailableLocations(photographer);
        GetPhotographerInfoResult photographerInfo = new GetPhotographerInfoResult(
            photographer.getName(),
            photographer.getBio(),
            specialties,
            locations
        );

        return GetUserInfoResult.of(user, photographerOptional, null, photographerInfo);
    }

    private static Photographer getPhotographer(Optional<Photographer> photographerOptional) {
        return photographerOptional
            .orElseThrow(() -> new UserException(UserErrorCode.PHOTOGRAPHER_NOT_FOUND));
    }

    private List<String> getSpecialties(Photographer photographer) {
        return specialtyRepository
            .findAllByPhotographer(photographer)
            .stream()
            .map(photographerSpecialty
                -> photographerSpecialty.getSpecialty().getCategory()
            )
            .toList();
    }

    private List<String> getAvailableLocations(Photographer photographer) {
        return locationRepository
            .findAllByPhotographer(photographer)
            .stream()
            .map(
                photographerAvailableLocation
                    -> photographerAvailableLocation.getAvailableLocation().getFullLocation()
            )
            .toList();
    }
}
