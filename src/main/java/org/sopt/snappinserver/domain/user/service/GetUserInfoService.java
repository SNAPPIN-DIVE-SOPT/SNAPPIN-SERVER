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
import org.sopt.snappinserver.domain.user.domain.entity.Onboarding;
import org.sopt.snappinserver.domain.user.domain.entity.User;
import org.sopt.snappinserver.domain.user.domain.exception.UserErrorCode;
import org.sopt.snappinserver.domain.user.domain.exception.UserException;
import org.sopt.snappinserver.domain.user.repository.OnboardingRepository;
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

    private static final String BASIC_PROFILE_IMAGE_KEY = "profile/basic_profile.png";

    private final UserRepository userRepository;
    private final PhotographerRepository photographerRepository;
    private final MoodRepository moodRepository;
    private final CurationRepositoryCustom curationRepository;
    private final PhotographerSpecialtyRepository specialtyRepository;
    private final PhotographerAvailableLocationRepository locationRepository;
    private final OnboardingRepository onboardingRepository;

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
        String profileImageUrl =
            (user.getProfileImageUrl() == null || user.getProfileImageUrl().isBlank())
                ? cloudFrontDomain + BASIC_PROFILE_IMAGE_KEY
                : cloudFrontDomain + user.getProfileImageUrl();
        boolean hasPhotographerProfile = photographerRepository.existsByUser(user);
        List<Long> moodIds = curationRepository.findTop3MoodIdsByUserId(user.getId());
        List<String> moodNames = getMoodNames(moodIds);
        Onboarding onboarding = getExistingOnboarding(user);
        GetClientInfoResult clientInfo = GetClientInfoResult.create(onboarding, moodNames);

        return GetUserInfoResult.of(
            user,
            profileImageUrl,
            hasPhotographerProfile,
            clientInfo,
            null
        );
    }

    private Onboarding getExistingOnboarding(User user) {
        return onboardingRepository.findByUser(user)
            .orElse(null);
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
        String profileImageUrl =
            (user.getProfileImageUrl() == null || user.getProfileImageUrl().isBlank())
                ? cloudFrontDomain + BASIC_PROFILE_IMAGE_KEY
                : cloudFrontDomain + user.getProfileImageUrl();
        boolean hasPhotographerProfile = photographerRepository.existsByUser(user);
        List<String> specialties = getSpecialties(photographer);
        List<String> locations = getAvailableLocations(photographer);
        GetPhotographerInfoResult photographerInfo = new GetPhotographerInfoResult(
            photographer.getNickname(),
            photographer.getBio(),
            specialties,
            locations
        );

        return GetUserInfoResult.of(
            user,
            profileImageUrl,
            hasPhotographerProfile,
            null,
            photographerInfo
        );
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
