package org.sopt.snappinserver.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.auth.domain.value.TokenPair;
import org.sopt.snappinserver.domain.auth.service.token.AuthTokenManager;
import org.sopt.snappinserver.domain.photographer.repository.PhotographerRepository;
import org.sopt.snappinserver.domain.user.domain.entity.User;
import org.sopt.snappinserver.domain.user.domain.exception.UserErrorCode;
import org.sopt.snappinserver.domain.user.domain.exception.UserException;
import org.sopt.snappinserver.domain.user.repository.UserRepository;
import org.sopt.snappinserver.domain.user.service.dto.request.SwitchUserRoleCommand;
import org.sopt.snappinserver.domain.user.service.dto.response.SwitchUserRoleResult;
import org.sopt.snappinserver.domain.user.service.usecase.SwitchUserRoleUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class SwitchUserRoleService implements SwitchUserRoleUseCase {

    private final UserRepository userRepository;
    private final PhotographerRepository photographerRepository;
    private final AuthTokenManager authTokenManager;

    public SwitchUserRoleResult switchUserRole(SwitchUserRoleCommand command) {
        User user = getExistingUser(command);
        validateHasPhotographerProfile(user);

        user.switchRole();

        TokenPair tokenPair = authTokenManager.issueTokenPair(user, command.userAgent());

        return SwitchUserRoleResult.from(tokenPair, user.getRole());
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

}
