package com.safezone.identity.internal.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.safezone.shared.domain.RoleType;
import com.safezone.shared.web.UnauthorizedException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Issues and verifies HS256 access tokens. Uses Nimbus (not the app's Jackson 3 {@code
 * tools.jackson} stack) since Nimbus does its own minimal JSON handling internally and
 * doesn't drag in a second, conflicting Jackson dependency.
 */
@Component
public class JwtService {

    private final MACSigner signer;
    private final MACVerifier verifier;
    private final long accessTokenTtlMinutes;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-ttl-minutes}") long accessTokenTtlMinutes) {
        try {
            var key = secret.getBytes(StandardCharsets.UTF_8);
            this.signer = new MACSigner(key);
            this.verifier = new MACVerifier(key);
        } catch (JOSEException e) {
            throw new IllegalStateException("jwt.secret must be at least 32 bytes long for HS256", e);
        }
        this.accessTokenTtlMinutes = accessTokenTtlMinutes;
    }

    public String issueAccessToken(UUID userId, UUID organizationId, String email, String fullName, Set<RoleType> roles) {
        var now = Instant.now();
        var claims = new JWTClaimsSet.Builder()
                .subject(userId.toString())
                .claim("org", organizationId.toString())
                .claim("email", email)
                .claim("name", fullName)
                .claim("roles", roles.stream().map(Enum::name).toList())
                .issueTime(Date.from(now))
                .expirationTime(Date.from(now.plus(accessTokenTtlMinutes, ChronoUnit.MINUTES)))
                .build();
        var jwt = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claims);
        try {
            jwt.sign(signer);
        } catch (JOSEException e) {
            throw new IllegalStateException("Failed to sign access token", e);
        }
        return jwt.serialize();
    }

    /** Throws {@link UnauthorizedException} on a bad signature, malformed token, or expiry. */
    @SuppressWarnings("unchecked")
    public AccessTokenClaims parseAndValidate(String token) {
        try {
            var jwt = SignedJWT.parse(token);
            if (!jwt.verify(verifier)) {
                throw new UnauthorizedException("Invalid token signature");
            }
            var claims = jwt.getJWTClaimsSet();
            if (claims.getExpirationTime() == null || claims.getExpirationTime().before(new Date())) {
                throw new UnauthorizedException("Token expired");
            }
            var roles = ((List<String>) claims.getClaim("roles"))
                    .stream().map(RoleType::valueOf).collect(Collectors.toSet());
            return new AccessTokenClaims(
                    UUID.fromString(claims.getSubject()),
                    UUID.fromString((String) claims.getClaim("org")),
                    (String) claims.getClaim("email"),
                    (String) claims.getClaim("name"),
                    Set.copyOf(roles));
        } catch (ParseException | JOSEException | IllegalArgumentException | ClassCastException e) {
            throw new UnauthorizedException("Malformed or invalid token");
        }
    }

    public long accessTokenTtlSeconds() {
        return accessTokenTtlMinutes * 60;
    }
}
