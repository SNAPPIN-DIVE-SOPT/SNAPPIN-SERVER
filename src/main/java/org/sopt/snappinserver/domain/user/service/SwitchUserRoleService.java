package org.sopt.snappinserver.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.auth.domain.exception.AuthErrorCode;
import org.sopt.snappinserver.domain.auth.domain.exception.AuthException;
import org.sopt.snappinserver.domain.auth.domain.value.TokenPair;
import org.sopt.snappinserver.domain.auth.infra.redis.RefreshTokenStore;
import org.sopt.snappinserver.domain.auth.infra.redis.RefreshTokenValue;
import org.sopt.snappinserver.domain.auth.service.token.AuthTokenManager;
import org.sopt.snappinserver.domain.user.service.dto.request.SwitchUserRoleCommand;
import org.sopt.snappinserver.domain.user.service.dto.response.SwitchUserRoleResult;
import org.sopt.snappinserver.domain.user.service.usecase.SwitchUserRoleUseCase;
import org.sopt.snappinserver.domain.photographer.repository.PhotographerRepository;
import org.sopt.snappinserver.domain.user.domain.entity.User;
import org.sopt.snappinserver.domain.user.domain.exception.UserErrorCode;
import org.sopt.snappinserver.domain.user.domain.exception.UserException;
import org.sopt.snappinserver.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class SwitchUserRoleService implements SwitchUserRoleUseCase {

    private final UserRepository userRepository;
    private final PhotographerRepository photographerRepository;
    private final RefreshTokenStore refreshTokenStore;
    private final AuthTokenManager authTokenManager;

    public SwitchUserRoleResult switchUserRole(SwitchUserRoleCommand command) {
        User user = getExistingUser(command);
        validateHasPhotographerProfile(user);

        user.switchRole();

        RefreshTokenValue refreshTokenValue = refreshTokenStore.find(command.refreshToken());
        validateCanDeleteRefreshToken(command, refreshTokenValue);
        refreshTokenStore.delete(command.refreshToken());

        TokenPair tokenPair = authTokenManager.issueTokenPair(user, command.userAgent());

        return SwitchUserRoleResult.from(tokenPair);
    }

    private User getExistingUser(SwitchUserRoleCommand command) {
        return userRepository.findById(command.userId())
            .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    }

    private void validateHasPhotographerProfile(User user) {
        if (!photographerRepository.existsByUser(user)) {
            throw new UserException(UserErrorCode.SWITCH_PROFILE_FORBIDDEN);
        }
    }

    private void validateCanDeleteRefreshToken(
        SwitchUserRoleCommand command,
        RefreshTokenValue refreshTokenValue
    ) {
        validateRefreshTokenValueExists(refreshTokenValue);
        authTokenManager.validateUserAgent(command.userAgent(), refreshTokenValue.userAgentHash());
    }

    private void validateRefreshTokenValueExists(RefreshTokenValue refreshTokenValue) {
        if (refreshTokenValue == null) {
            throw new AuthException(AuthErrorCode.INVALID_REFRESH_TOKEN);
        }
    }

}
