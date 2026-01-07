package org.sopt.snappinserver.domain.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import org.sopt.snappinserver.domain.auth.domain.exception.AuthErrorCode;
import org.sopt.snappinserver.domain.auth.domain.exception.AuthException;
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
        @Value("${jwt.expiration_time}") long accessTokenExpTime
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
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(now.plusSeconds(expireSeconds)))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (JwtException | IllegalArgumentException e) {
            throw new AuthException(AuthErrorCode.INVALID_JWT_TOKEN);
        }
    }

    public Long getUserId(String token) {
        return parseClaims(token).get(CLAIM_USER_ID, Long.class);
    }

    public void validateToken(String token) {
        parseClaims(token);
    }
}
