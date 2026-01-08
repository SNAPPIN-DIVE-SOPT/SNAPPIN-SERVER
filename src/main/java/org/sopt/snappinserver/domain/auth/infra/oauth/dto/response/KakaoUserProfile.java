package org.sopt.snappinserver.domain.auth.infra.oauth.dto.response;

import org.sopt.snappinserver.domain.auth.domain.exception.AuthErrorCode;
import org.sopt.snappinserver.domain.auth.domain.exception.AuthException;

public record KakaoUserProfile(
    String socialId,
    String nickname,
    String profileImage
    ) {

    public static KakaoUserProfile from(KakaoUserInfo info) {
        if (info.kakaoAccount() == null || info.kakaoAccount().profile() == null) {
            throw new AuthException(AuthErrorCode.KAKAO_PROFILE_NOT_PROVIDED);
        }
        return new KakaoUserProfile(
            info.id().toString(),
            info.kakaoAccount().profile().nickname(),
            info.kakaoAccount().profile().profileImageUrl()
        );
    }
}
