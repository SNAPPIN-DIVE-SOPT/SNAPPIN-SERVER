package org.sopt.snappinserver.domain.auth.service.usecase;

import org.sopt.snappinserver.domain.auth.service.dto.response.LoginResult;

public interface LoginUseCase {

    LoginResult kakaoLogin(String redirectUri, String accessCode, String userAgent);
}
