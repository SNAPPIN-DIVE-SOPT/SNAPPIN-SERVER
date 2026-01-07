package org.sopt.snappinserver.domain.auth.infra.redis;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RefreshTokenStore {

    private static final String KEY_PREFIX = "refresh:";

    private final RedisTemplate<String, Object> redisTemplate;

    public void save(String refreshToken, RefreshTokenValue value, Duration ttl) {
        redisTemplate.opsForValue().set(KEY_PREFIX + refreshToken, value, ttl);
    }

    public RefreshTokenValue get(String refreshToken) {
        Object value = redisTemplate.opsForValue().get(KEY_PREFIX + refreshToken);
        return (RefreshTokenValue) value;
    }

    public void delete(String refreshToken) {
        redisTemplate.delete(KEY_PREFIX + refreshToken);
    }
}
