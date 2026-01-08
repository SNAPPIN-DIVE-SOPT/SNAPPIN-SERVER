package org.sopt.snappinserver.domain.auth.infra.redis;

import java.io.Serializable;

public record RefreshTokenValue(
    Long userId,
    String userAgentHash
) implements Serializable {

}
