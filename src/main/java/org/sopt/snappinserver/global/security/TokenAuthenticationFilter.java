package org.sopt.snappinserver.global.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.auth.infra.jwt.CustomUserInfo;
import org.sopt.snappinserver.domain.auth.infra.jwt.JwtProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    public static final String AUTH_ERROR_ATTR = "AUTH_ERROR";

    private final JwtProvider jwtProvider;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        return path.startsWith("/api/v1/auth/")
            || path.equals("/api/v1/photos/process");
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = header.substring(7);

        try {
            Claims claims = jwtProvider.parseAndValidate(accessToken);

            Long userId = jwtProvider.getUserId(claims);
            String role = jwtProvider.getRole(claims);

            CustomUserInfo principal = new CustomUserInfo(userId, role);

            GrantedAuthority authority =
                new SimpleGrantedAuthority("ROLE_" + role);

            Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                    principal,
                    null,
                    List.of(authority)
                );

            SecurityContextHolder.getContext()
                .setAuthentication(authentication);
        } catch (ExpiredJwtException e) {
            request.setAttribute(AUTH_ERROR_ATTR, "EXPIRED_ACCESS_TOKEN");
            SecurityContextHolder.clearContext();
        } catch (JwtException | IllegalArgumentException e) {
            request.setAttribute(AUTH_ERROR_ATTR, "INVALID_ACCESS_TOKEN");
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}
