package org.sopt.snappinserver.domain.auth.infra.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

    private static final String CLAIM_USER_ID = "userId";
    private static final String CLAIM_ROLE = "role";

    private final Key key;
    private final long accessTokenExpTime;

    public JwtProvider(
        @Value("${jwt.secret}") String secretKey,
        @Value("${jwt.expiration-time}") long accessTokenExpTime
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpTime = accessTokenExpTime;
    }

    public String createAccessToken(CustomUserInfo userInfo) {
        return createToken(userInfo, accessTokenExpTime);
    }

    private String createToken(CustomUserInfo userInfo, long expireSeconds) {
        Instant now = Instant.now();

        return Jwts.builder()
            .claim(CLAIM_USER_ID, userInfo.userId())
            .claim(CLAIM_ROLE, userInfo.role())
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plusSeconds(expireSeconds)))
            .signWith(key)
            .compact();
    }
}
