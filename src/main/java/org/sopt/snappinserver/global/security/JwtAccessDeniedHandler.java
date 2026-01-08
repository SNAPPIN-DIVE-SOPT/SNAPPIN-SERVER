package org.sopt.snappinserver.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import org.sopt.snappinserver.global.exception.CommonErrorCode;
import org.sopt.snappinserver.global.response.dto.ApiResponseBody;
import org.sopt.snappinserver.global.response.dto.ErrorMeta;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public JwtAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(
        HttpServletRequest httpServletRequest,
        HttpServletResponse httpServletResponse,
        AccessDeniedException exception
    ) throws IOException {
        ErrorMeta meta = new ErrorMeta(
            httpServletRequest.getRequestURI(),
            Instant.now()
        );
        ApiResponseBody<Void, ErrorMeta> body = ApiResponseBody.onFailure(
            CommonErrorCode.FORBIDDEN,
            meta
        );

        httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter()
            .write(objectMapper.writeValueAsString(body));
    }
}

