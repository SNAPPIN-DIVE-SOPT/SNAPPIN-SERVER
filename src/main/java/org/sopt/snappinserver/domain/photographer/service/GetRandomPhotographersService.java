package org.sopt.snappinserver.domain.photographer.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.photographer.domain.entity.Photographer;
import org.sopt.snappinserver.domain.photographer.domain.entity.PhotographerSpecialty;
import org.sopt.snappinserver.domain.photographer.repository.PhotographerRepository;
import org.sopt.snappinserver.domain.photographer.repository.PhotographerSpecialtyRepository;
import org.sopt.snappinserver.domain.photographer.service.dto.response.GetRandomPhotographersResult;
import org.sopt.snappinserver.domain.photographer.service.usecase.GetRandomPhotographersUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class GetRandomPhotographersService implements GetRandomPhotographersUseCase {

    private static final String baseProfileImageKey = "profile/basic_profile.png";

    private final PhotographerRepository photographerRepository;
    private final PhotographerSpecialtyRepository photographerSpecialtyRepository;

    @Value("${cloud.aws.cloud-front.domain}")
    private String cloudFrontDomain;

    public List<GetRandomPhotographersResult> getRandomPhotographersResult() {
        List<Photographer> photographers = photographerRepository.findRandom(5);
        return photographers.stream()
            .map(photographer -> {
                String profileImageUrl = (photographer.getUser().getProfileImageUrl() == null)
                    ? cloudFrontDomain + baseProfileImageKey
                    : cloudFrontDomain + photographer.getUser().getProfileImageUrl();
                List<PhotographerSpecialty> specialties = photographerSpecialtyRepository
                    .findAllByPhotographer(photographer);

                return GetRandomPhotographersResult.of(photographer, profileImageUrl, specialties);
            })
            .toList();
    }
}
