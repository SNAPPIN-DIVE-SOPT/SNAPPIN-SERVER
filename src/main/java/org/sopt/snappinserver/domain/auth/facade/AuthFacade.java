package org.sopt.snappinserver.domain.auth.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.auth.service.dto.response.LoginResult;
import org.sopt.snappinserver.domain.auth.service.dto.response.LoginWithPhotographerProfileResult;
import org.sopt.snappinserver.domain.auth.service.usecase.LoginUseCase;
import org.sopt.snappinserver.domain.photographer.service.dto.response.GetPhotographerExistenceResult;
import org.sopt.snappinserver.domain.photographer.service.usecase.GetPhotographerExistenceUseCase;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthFacade {

    private final LoginUseCase loginUseCase;
    private final GetPhotographerExistenceUseCase getPhotographerExistenceUseCase;

    public LoginWithPhotographerProfileResult loginWithPhotographerProfile(
        String redirectUri,
        String accessCode,
        String userAgent
    ) {
        LoginResult loginResult = loginUseCase.kakaoLogin(redirectUri, accessCode, userAgent);
        GetPhotographerExistenceResult photographerExistence = getPhotographerExistenceUseCase
            .getHasPhotographerProfile(loginResult.userId());

        return LoginWithPhotographerProfileResult.of(loginResult, photographerExistence);
    }

}
