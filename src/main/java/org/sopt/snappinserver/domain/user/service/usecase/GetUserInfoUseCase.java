package org.sopt.snappinserver.domain.user.service.usecase;

import org.sopt.snappinserver.domain.user.service.dto.response.GetUserInfoResult;

public interface GetUserInfoUseCase {

    GetUserInfoResult getUserInfo(Long userId);
}
