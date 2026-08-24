package com.safezone.identity.internal.service;

import com.safezone.identity.internal.domain.AppUser;

public record AuthResult(String accessToken, String refreshToken, long expiresIn, AppUser user) {
}
