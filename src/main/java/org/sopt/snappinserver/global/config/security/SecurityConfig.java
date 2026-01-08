package org.sopt.snappinserver.global.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String[] SWAGGER_URLS = {
        "/swagger-ui/**",
        "/swagger-resources/**",
        "/v3/api-docs/**"
    };

    private static final String[] PUBLIC_GET_URLS = {
        "/api/v1/categories",
        "/api/v1/home/recommendation",
        "/api/v1/moods",

        "/api/v1/photographers/**",
        "/api/v1/places/**",
        "/api/v1/portfolios/*",
        "/api/v1/products/**"
    };

    private static final String[] AUTHENTICATED_URLS = {
        "/api/v1/portfolios/recommendation",
        "/api/v1/products/*/reservations"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(Customizer.withDefaults())
            .sessionManagement(sess ->
                sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)

            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/health").permitAll()
                .requestMatchers(SWAGGER_URLS).permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers(AUTHENTICATED_URLS).authenticated()
                .requestMatchers(HttpMethod.POST, "/api/v1/auth/login/kakao").permitAll()
                .requestMatchers(HttpMethod.GET, PUBLIC_GET_URLS).permitAll()
                .anyRequest().authenticated()
            );

        return http.build();
    }
}

