package org.sopt.snappinserver.domain.auth.infra.oauth.dto.response;

public record KakaoAccount(
    boolean profile_nickname_needs_agreement,
    boolean profile_image_needs_agreement,
    Profile profile
) {

}
