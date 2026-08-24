package com.safezone.identity.internal.web.dto;

import com.safezone.identity.internal.service.AuthResult;

public record AuthResponse(String accessToken, String refreshToken, long expiresIn, UserResponse user) {

    public static AuthResponse from(AuthResult result) {
        return new AuthResponse(result.accessToken(), result.refreshToken(), result.expiresIn(), UserResponse.from(result.user()));
    }
}
