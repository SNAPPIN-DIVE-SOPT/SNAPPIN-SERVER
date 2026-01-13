package org.sopt.snappinserver.domain.user.service.usecase;

import org.sopt.snappinserver.domain.user.service.dto.response.SwitchRoleResult;

public interface SwitchRoleUseCase {

    SwitchRoleResult switchUserRole(Long userId);
}
