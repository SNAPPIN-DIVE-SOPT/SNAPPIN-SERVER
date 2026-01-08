package org.sopt.snappinserver.domain.auth.infra.oauth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Profile(

    @JsonProperty("nickname")
    String nickname,

    @JsonProperty("thumbnail_image_url")
    String thumbnailImageUrl,

    @JsonProperty("profile_image_url")
    String profileImageUrl,

    @JsonProperty("is_default_image")
    boolean isDefaultImage,

    @JsonProperty("is_default_nickname")
    boolean isDefaultNickname
) {

}
