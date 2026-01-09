package org.sopt.snappinserver.domain.auth.infra.jwt;

import org.sopt.snappinserver.domain.user.domain.entity.User;

public record CustomUserInfo(
    Long userId,
    String role
) {

    public static CustomUserInfo from(User user) {
        return new CustomUserInfo(
            user.getId(),
            user.getRole().name()
        );
    }
}
