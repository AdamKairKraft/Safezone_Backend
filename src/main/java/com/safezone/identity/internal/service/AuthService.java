package com.safezone.identity.internal.service;

public interface AuthService {

    AuthResult login(String email, String password);

    AuthResult refresh(String rawRefreshToken);

    void logout(String rawRefreshToken);
}
