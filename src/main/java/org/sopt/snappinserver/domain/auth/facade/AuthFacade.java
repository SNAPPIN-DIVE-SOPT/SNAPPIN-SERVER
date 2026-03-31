package org.sopt.snappinserver.domain.auth.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.auth.service.dto.response.LoginResult;
import org.sopt.snappinserver.domain.auth.service.dto.response.LoginWithPhotographerProfileResult;
import org.sopt.snappinserver.domain.auth.service.usecase.LoginUseCase;
import org.sopt.snappinserver.domain.photographer.service.dto.response.GetPhotographerExistenceResult;
import org.sopt.snappinserver.domain.photographer.service.usecase.GetPhotographerExistenceUseCase;
import org.sopt.snappinserver.domain.user.service.dto.response.GetOnboardingExistsResult;
import org.sopt.snappinserver.domain.user.service.usecase.GetOnboardingExistsUseCase;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthFacade {

    private final LoginUseCase loginUseCase;
    private final GetPhotographerExistenceUseCase getPhotographerExistenceUseCase;
    private final GetOnboardingExistsUseCase getOnboardingExistsUseCase;

    public LoginWithPhotographerProfileResult loginWithPhotographerProfile(
        String redirectUri,
        String accessCode,
        String userAgent
    ) {
        LoginResult loginResult = loginUseCase.kakaoLogin(redirectUri, accessCode, userAgent);
        GetPhotographerExistenceResult photographerExistence = getPhotographerExistenceUseCase
            .getHasPhotographerProfile(loginResult.userId());
        GetOnboardingExistsResult onboardingExistence = getOnboardingExistsUseCase
            .getOnboardingExists(loginResult.userId());

        return LoginWithPhotographerProfileResult.of(
            loginResult,
            photographerExistence,
            onboardingExistence
        );
    }

}
