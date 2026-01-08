package org.sopt.snappinserver.domain.auth.service.usecase;

public interface LogoutUseCase {

    void logout(Long userId, String refreshToken);
}
