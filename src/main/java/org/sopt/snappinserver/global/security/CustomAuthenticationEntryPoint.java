package org.sopt.snappinserver.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.auth.domain.exception.AuthErrorCode;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.sopt.snappinserver.global.response.dto.ErrorMeta;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException authException
    ) throws IOException {
        Object reason = request.getAttribute(TokenAuthenticationFilter.AUTH_ERROR_ATTR);

        AuthErrorCode code;
        if ("EXPIRED_ACCESS_TOKEN".equals(reason)) {
            code = AuthErrorCode.EXPIRED_ACCESS_TOKEN;
        } else if ("INVALID_ACCESS_TOKEN".equals(reason)) {
            code = AuthErrorCode.INVALID_ACCESS_TOKEN;
        } else {
            code = AuthErrorCode.LOGIN_REQUIRED;
        }

        ApiResponseBody<Void, ErrorMeta> body =
            ApiResponseBody.onFailure(
                code,
                new ErrorMeta(request.getRequestURI(), Instant.now())
            );

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}
