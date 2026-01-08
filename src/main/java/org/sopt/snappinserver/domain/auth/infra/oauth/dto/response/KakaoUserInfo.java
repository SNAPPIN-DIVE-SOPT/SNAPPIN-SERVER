package org.sopt.snappinserver.domain.auth.infra.oauth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoUserInfo(

    @JsonProperty("id")
    Long id,

    @JsonProperty("connected_at")
    String connectedAt,

    @JsonProperty("properties")
    Properties properties,

    @JsonProperty("kakao_account")
    KakaoAccount kakaoAccount
) {

}
