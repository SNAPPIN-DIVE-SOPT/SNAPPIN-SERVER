package org.sopt.snappinserver.domain.auth.infra.oauth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Properties(

    @JsonProperty("nickname")
    String nickname,

    @JsonProperty("profile_image")
    String profileImage,

    @JsonProperty("thumbnail_image")
    String thumbnailImage
) {

}
