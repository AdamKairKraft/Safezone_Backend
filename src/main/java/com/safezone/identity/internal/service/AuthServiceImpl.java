package com.safezone.identity.internal.service;

import com.safezone.identity.internal.domain.AppUser;
import com.safezone.identity.internal.domain.RefreshToken;
import com.safezone.identity.internal.repository.AppUserRepository;
import com.safezone.identity.internal.repository.RefreshTokenRepository;
import com.safezone.identity.internal.security.JwtService;
import com.safezone.shared.web.UnauthorizedException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.HexFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
class AuthServiceImpl implements AuthService {

    private static final SecureRandom RANDOM = new SecureRandom();

    private final AppUserRepository appUserRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final long refreshTokenTtlDays;

    AuthServiceImpl(
            AppUserRepository appUserRepository,
            RefreshTokenRepository refreshTokenRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            @Value("${jwt.refresh-token-ttl-days}") long refreshTokenTtlDays) {
        this.appUserRepository = appUserRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenTtlDays = refreshTokenTtlDays;
    }

    @Override
    public AuthResult login(String email, String password) {
        var user = appUserRepository.findByEmail(email)
                .filter(candidate -> passwordEncoder.matches(password, candidate.getPasswordHash()))
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));
        return issueTokens(user);
    }

    @Override
    public AuthResult refresh(String rawRefreshToken) {
        var stored = refreshTokenRepository.findByTokenHash(hash(rawRefreshToken))
                .filter(RefreshToken::isUsable)
                .orElseThrow(() -> new UnauthorizedException("Invalid or expired refresh token"));
        // Rotation: this token is single-use even if it hasn't expired yet.
        stored.revoke();
        var user = appUserRepository.findById(stored.getAppUserId())
                .orElseThrow(() -> new UnauthorizedException("Invalid or expired refresh token"));
        return issueTokens(user);
    }

    @Override
    public void logout(String rawRefreshToken) {
        refreshTokenRepository.findByTokenHash(hash(rawRefreshToken)).ifPresent(RefreshToken::revoke);
    }

    private AuthResult issueTokens(AppUser user) {
        var accessToken = jwtService.issueAccessToken(
                user.getId(), user.getOrganizationId(), user.getEmail(), user.getFullName(), user.getRoles());
        var rawRefreshToken = generateOpaqueToken();
        var expiresAt = Instant.now().plus(refreshTokenTtlDays, ChronoUnit.DAYS);
        refreshTokenRepository.save(new RefreshToken(user.getId(), hash(rawRefreshToken), expiresAt));
        return new AuthResult(accessToken, rawRefreshToken, jwtService.accessTokenTtlSeconds(), user);
    }

    private static String generateOpaqueToken() {
        var bytes = new byte[32];
        RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private static String hash(String value) {
        try {
            var digest = MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
