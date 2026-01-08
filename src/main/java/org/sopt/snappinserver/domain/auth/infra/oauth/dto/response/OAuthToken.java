package org.sopt.snappinserver.domain.auth.infra.oauth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OAuthToken(

    @JsonProperty("access_token")
    String accessToken,

    @JsonProperty("token_type")
    String tokenType,

    @JsonProperty("refresh_token")
    String refreshToken,

    @JsonProperty("id_token")
    String idToken,

    @JsonProperty("expires_in")
    int expiresIn,

    @JsonProperty("scope")
    String scope,

    @JsonProperty("refresh_token_expires_in")
    int refreshTokenExpiresIn
) {

}
