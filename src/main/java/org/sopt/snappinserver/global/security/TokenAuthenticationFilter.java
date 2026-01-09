package org.sopt.snappinserver.global.security;

import static org.sopt.snappinserver.domain.auth.domain.exception.AuthErrorCode.EXPIRED_ACCESS_TOKEN;
import static org.sopt.snappinserver.domain.auth.domain.exception.AuthErrorCode.INVALID_ACCESS_TOKEN;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.auth.domain.exception.AuthErrorCode;
import org.sopt.snappinserver.domain.auth.infra.jwt.CustomUserInfo;
import org.sopt.snappinserver.domain.auth.infra.jwt.JwtProvider;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.sopt.snappinserver.global.response.dto.ErrorMeta;
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

    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/api/v1/auth/reissue") || path.startsWith("/api/v1/auth/login");
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
            sendError(response, request, EXPIRED_ACCESS_TOKEN);
            return;
        } catch (MalformedJwtException | IllegalArgumentException | UnsupportedJwtException e) {
            sendError(response, request, INVALID_ACCESS_TOKEN);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void sendError(
        HttpServletResponse response,
        HttpServletRequest request,
        AuthErrorCode errorCode
    ) throws IOException {

        ErrorMeta meta = new ErrorMeta(
            request.getRequestURI(),
            Instant.now()
        );

        ApiResponseBody<Void, ErrorMeta> body =
            ApiResponseBody.onFailure(errorCode, meta);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(
            objectMapper.writeValueAsString(body)
        );
    }

}
