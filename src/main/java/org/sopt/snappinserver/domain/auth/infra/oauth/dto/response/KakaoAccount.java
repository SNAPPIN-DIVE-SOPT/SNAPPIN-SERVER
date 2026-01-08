package org.sopt.snappinserver.domain.auth.infra.oauth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoAccount(

    @JsonProperty("profile_nickname_needs_agreement")
    boolean profileNicknameNeedsAgreement,

    @JsonProperty("profile_image_needs_agreement")
    boolean profileImageNeedsAgreement,

    @JsonProperty("profile")
    Profile profile
) {

}
