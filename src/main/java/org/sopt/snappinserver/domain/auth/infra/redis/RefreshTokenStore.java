package org.sopt.snappinserver.domain.auth.infra.redis;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RefreshTokenStore {

    private static final String KEY_PREFIX = "refresh:";

    private final RedisTemplate<String, RefreshTokenValue> redisTemplate;

    public void save(String refreshToken, RefreshTokenValue value, Duration ttl) {
        redisTemplate.opsForValue()
            .set(key(refreshToken), value, ttl);
    }

    public RefreshTokenValue find(String refreshToken) {
        return redisTemplate.opsForValue()
            .get(key(refreshToken));
    }

    public void delete(String refreshToken) {
        redisTemplate.delete(key(refreshToken));
    }

    private String key(String refreshToken) {
        return KEY_PREFIX + refreshToken;
    }
}
