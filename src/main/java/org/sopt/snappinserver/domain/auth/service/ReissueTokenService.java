package org.sopt.snappinserver.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.auth.domain.exception.AuthErrorCode;
import org.sopt.snappinserver.domain.auth.domain.exception.AuthException;
import org.sopt.snappinserver.domain.auth.domain.value.TokenPair;
import org.sopt.snappinserver.domain.auth.infra.redis.RefreshTokenStore;
import org.sopt.snappinserver.domain.auth.infra.redis.RefreshTokenValue;
import org.sopt.snappinserver.domain.auth.service.dto.response.ReissueTokenResult;
import org.sopt.snappinserver.domain.auth.service.token.AuthTokenManager;
import org.sopt.snappinserver.domain.auth.service.usecase.ReissueTokenUseCase;
import org.sopt.snappinserver.domain.user.domain.entity.User;
import org.sopt.snappinserver.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class ReissueTokenService implements ReissueTokenUseCase {

    private final RefreshTokenStore refreshTokenStore;
    private final AuthTokenManager authTokenManager;
    private final UserRepository userRepository;

    @Override
    public ReissueTokenResult reissueToken(String refreshToken, String userAgent) {
        RefreshTokenValue refreshTokenValue = refreshTokenStore.find(refreshToken);
        validateRefreshTokenValueExists(refreshTokenValue);

        authTokenManager.validateUserAgent(userAgent, refreshTokenValue.userAgentHash());
        User user = userRepository.findById(refreshTokenValue.userId())
            .orElseThrow(() -> new AuthException(AuthErrorCode.USER_NOT_FOUND));
        refreshTokenStore.delete(refreshToken);

        TokenPair tokenPair = authTokenManager.issueTokenPair(user, userAgent);

        return ReissueTokenResult.from(tokenPair);
    }

    private void validateRefreshTokenValueExists(RefreshTokenValue refreshTokenValue) {
        if (refreshTokenValue == null) {
            throw new AuthException(AuthErrorCode.INVALID_REFRESH_TOKEN);
        }
    }

}
