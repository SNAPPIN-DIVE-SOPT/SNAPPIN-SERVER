package org.sopt.snappinserver.domain.auth.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.global.response.code.common.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    // 400 BAD REQUEST
    USER_REQUIRED(400, "AUTH_400_001", "유저는 필수입니다."),
    SOCIAL_PROVIDER_REQUIRED(400, "AUTH_400_002", "소셜 로그인 제공자는 필수입니다."),
    PROVIDER_ID_REQUIRED(400, "AUTH_400_003", "소셜 로그인 제공자 측 식별자는 필수입니다."),
    PROVIDER_ID_TOO_LONG(400, "AUTH_400_004", "소셜 로그인 제공자 측 식별자 길이는 300자 이하입니다."),
    REFRESH_TOKEN_REQUIRED(400, "AUTH_400_005", "리프레시 토큰은 필수입니다."),
    REFRESH_TOKEN_TOO_LONG(400, "AUTH_400_006", "리프레시 토큰 길이는 512자 이하입니다."),
    EXPIRES_AT_REQUIRED(400, "AUTH_400_007", "토큰 만료일은 필수입니다."),
    USER_AGENT_LENGTH_TOO_LONG(400, "AUTH_400_008", "로그인 기기 정보(UserAgent) 길이는 512자 이하입니다."),
    SHA_256_UNSUPPORTED(400, "AUTH_400_009", "SHA256 해싱은 지원되지 않습니다."),
    KAKAO_PROFILE_NOT_PROVIDED(400, "AUTH_400_010", "카카오 프로필이 정상적으로 제공되지 않았습니다."),

    // 401 UNAUTHORIZED
    EXPIRED_JWT_TOKEN(401, "AUTH_401_001", "토큰이 만료되었습니다."),
    INVALID_JWT_TOKEN(401, "AUTH_401_002", "잘못된 토큰입니다."),
    INVALID_OAUTH_TOKEN(401, "AUTH_401_003", "카카오 로그인 접근 실패"),
    INVALID_REFRESH_TOKEN(401, "AUTH_401_004", "잘못된 리프레시 토큰입니다."),
    REFRESH_TOKEN_COOKIE_REQUIRED(401, "AUTH_401_005", "리프레시 토큰 쿠키는 필수입니다."),

    // 403 FORBIDDEN

    // 404 NOT FOUND
    USER_NOT_FOUND(404, "AUTH_404_001", "해당 유저를 찾을 수 없습니다."),

    ;

    private final int status;
    private final String code;
    private final String message;
}
