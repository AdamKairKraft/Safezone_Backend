package com.safezone.identity.internal.security;

import com.safezone.shared.web.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

/** Keeps a 401's body in the same {@link ApiError} shape as every other API error. */
@Component
class AuthenticationEntryPointJson implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    AuthenticationEntryPointJson(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(
                ApiError.of("Authentication required", HttpStatus.UNAUTHORIZED.value())));
    }
}
