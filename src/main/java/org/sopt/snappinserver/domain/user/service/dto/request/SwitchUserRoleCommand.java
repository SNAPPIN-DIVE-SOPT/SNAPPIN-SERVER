package org.sopt.snappinserver.domain.user.service.dto.request;

public record SwitchUserRoleCommand(
    Long userId,
    String userAgent
) {

}
