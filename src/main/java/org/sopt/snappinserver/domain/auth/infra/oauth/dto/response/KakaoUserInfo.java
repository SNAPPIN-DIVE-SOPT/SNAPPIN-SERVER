package org.sopt.snappinserver.domain.auth.infra.oauth.dto.response;

public record KakaoUserInfo(
    Long id,
    String connected_at,
    Properties properties,
    KakaoAccount kakao_account
) {

}
