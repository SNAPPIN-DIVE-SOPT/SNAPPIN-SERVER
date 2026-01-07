package org.sopt.snappinserver.domain.auth.infra.oauth.dto.response;

public record Profile(
    String nickname,
    String thumbnail_image_url,
    String profile_image_url,
    boolean is_default_image,
    boolean is_default_nickname
) {

}
