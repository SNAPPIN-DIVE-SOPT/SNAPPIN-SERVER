package org.sopt.snappinserver.domain.user.service.usecase;

import org.sopt.snappinserver.domain.user.service.dto.request.SwitchUserRoleCommand;
import org.sopt.snappinserver.domain.user.service.dto.response.SwitchUserRoleResult;

public interface SwitchUserRoleUseCase {

    SwitchUserRoleResult switchUserRole(SwitchUserRoleCommand command);
}
