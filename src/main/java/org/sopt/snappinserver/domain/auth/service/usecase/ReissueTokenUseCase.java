package org.sopt.snappinserver.domain.auth.service.usecase;

import org.sopt.snappinserver.domain.auth.service.dto.response.ReissueTokenResult;

public interface ReissueTokenUseCase {

    ReissueTokenResult reissueToken(String refreshToken, String userAgent);

}
