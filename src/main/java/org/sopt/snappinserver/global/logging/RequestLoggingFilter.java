package org.sopt.snappinserver.global.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class RequestLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String requestId = generateRequestId();
        MDC.put("requestId", requestId);

        long start = System.currentTimeMillis();

        try {
            filterChain.doFilter(request, response);
        } finally {
            long took = System.currentTimeMillis() - start;

            String method = request.getMethod();
            String uri = request.getRequestURI(); // TODO API 명세 설계 완료 후 특정 API에 한해 query Parameter 추가
            int status = response.getStatus();
            String ip = extractClientIp(request);
            String principal = resolvePrincipal();

            if (status >= 500) {
                log.error("[{}] {} {} -> status={} user={} ip={} took={}ms",
                    requestId, method, uri, status, principal, ip, took);
            } else if (status >= 400) {
                log.warn("[{}] {} {} -> status={} user={} ip={} took={}ms",
                    requestId, method, uri, status, principal, ip, took);
            } else {
                log.info("[{}] {} {} -> status={} user={} ip={} took={}ms",
                    requestId, method, uri, status, principal, ip, took);
            }

            MDC.clear();
        }
    }

    private String generateRequestId() {
        return UUID.randomUUID().toString();
    }

    private String extractClientIp(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private String resolvePrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return "anonymous";
        }

        return "authenticated";
    }
}
