package org.sopt.snappinserver.domain.user.service;

import java.util.List;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class GetUserInfoService implements GetUserInfoUseCase {

    private static final String basicProfileImageKey = "profile/basic_profile.png";

    private final UserRepository userRepository;
    private final PhotographerRepository photographerRepository;
    private final MoodRepository moodRepository;
    private final CurationRepositoryCustom curationRepository;
    private final PhotographerSpecialtyRepository specialtyRepository;
    private final PhotographerAvailableLocationRepository locationRepository;

    @Value("${cloud.aws.cloud-front.domain}")
    private String cloudFrontDomain;

    @Override
    public GetUserInfoResult getUserInfo(Long userId) {
        User user = getUser(userId);
        Photographer photographer = photographerRepository.findByUser(user)
            .orElse(null);

        return user.isLoginByClient()
            ? getClientInfo(user)
            : getPhotographerInfo(user, photographer);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    }

    private GetUserInfoResult getClientInfo(
        User user
    ) {
        String profileImageUrl = (user.getProfileImageUrl() == null)
            ? cloudFrontDomain + basicProfileImageKey
            : user.getProfileImageUrl();
        List<Long> moodIds = curationRepository.findTop3MoodIdsByUserId(user.getId());
        List<String> moodNames = getMoodNames(moodIds);
        GetClientInfoResult clientInfo = new GetClientInfoResult(user.getName(), moodNames);

        return GetUserInfoResult.of(user, profileImageUrl, null, clientInfo, null);
    }

    private List<String> getMoodNames(List<Long> moodIds) {
        return moodRepository.findAllById(moodIds)
            .stream()
            .map(Mood::getName)
            .toList();
    }

    private GetUserInfoResult getPhotographerInfo(
        User user,
        Photographer photographer
    ) {
        String profileImageUrl = (user.getProfileImageUrl() == null)
            ? cloudFrontDomain + basicProfileImageKey
            : user.getProfileImageUrl();
        List<String> specialties = getSpecialties(photographer);
        List<String> locations = getAvailableLocations(photographer);
        GetPhotographerInfoResult photographerInfo = new GetPhotographerInfoResult(
            photographer.getName(),
            photographer.getBio(),
            specialties,
            locations
        );

        return GetUserInfoResult.of(user, profileImageUrl, photographer, null, photographerInfo);
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
