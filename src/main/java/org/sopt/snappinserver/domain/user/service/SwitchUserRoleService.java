package org.sopt.snappinserver.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.user.service.dto.response.SwitchRoleResult;
import org.sopt.snappinserver.domain.user.service.usecase.SwitchRoleUseCase;
import org.sopt.snappinserver.domain.photographer.repository.PhotographerRepository;
import org.sopt.snappinserver.domain.user.domain.entity.User;
import org.sopt.snappinserver.domain.user.domain.exception.UserErrorCode;
import org.sopt.snappinserver.domain.user.domain.exception.UserException;
import org.sopt.snappinserver.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SwitchUserRoleService implements SwitchRoleUseCase {

    private final UserRepository userRepository;
    private final PhotographerRepository photographerRepository;

    public SwitchRoleResult switchUserRole(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        if (!photographerRepository.existsByUser(user)) {
            throw new UserException(UserErrorCode.SWITCH_PROFILE_FORBIDDEN);
        }
        // 유저 역할 바꿔서 저장
        user.switchRole();
        // 토큰 재발급

        return null;
    }

}
