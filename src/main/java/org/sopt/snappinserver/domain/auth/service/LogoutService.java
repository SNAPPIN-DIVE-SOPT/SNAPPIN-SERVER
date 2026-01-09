package org.sopt.snappinserver.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.auth.service.token.AuthTokenManager;
import org.sopt.snappinserver.domain.auth.service.usecase.LogoutUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class LogoutService  implements LogoutUseCase {

    private final AuthTokenManager authTokenManager;

    @Override
    public void logout(Long userId, String refreshToken) {
        authTokenManager.logout(userId, refreshToken);
    }

}
