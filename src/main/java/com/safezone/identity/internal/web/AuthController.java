package com.safezone.identity.internal.web;

import com.safezone.identity.internal.service.AuthService;
import com.safezone.identity.internal.web.dto.AuthResponse;
import com.safezone.identity.internal.web.dto.LoginRequest;
import com.safezone.identity.internal.web.dto.RefreshRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Permitted without a bearer token - see SecurityConfig ("/api/auth/**" is public).
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return AuthResponse.from(authService.login(request.email(), request.password()));
    }

    @PostMapping("/refresh")
    public AuthResponse refresh(@Valid @RequestBody RefreshRequest request) {
        return AuthResponse.from(authService.refresh(request.refreshToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody RefreshRequest request) {
        authService.logout(request.refreshToken());
        return ResponseEntity.noContent().build();
    }
}
