package org.sopt.snappinserver.domain.auth.infra.oauth.dto.response;

public record Properties(
    String nickname,
    String profileImage,
    String thumbnailImage
) {

}
