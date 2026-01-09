package org.sopt.snappinserver.api.auth.dto.response;

import org.sopt.snappinserver.domain.auth.service.dto.response.ReissueTokenResult;

public record CreateAccessTokenResponse(
    String accessToken
) {

    public static CreateAccessTokenResponse from(ReissueTokenResult reissueTokenResult) {
        return new CreateAccessTokenResponse(reissueTokenResult.accessToken());
    }
}
