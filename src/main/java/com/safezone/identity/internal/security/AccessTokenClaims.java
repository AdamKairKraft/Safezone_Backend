package com.safezone.identity.internal.security;

import com.safezone.shared.domain.RoleType;
import java.util.Set;
import java.util.UUID;

/** Decoded, signature-verified contents of an access token. */
public record AccessTokenClaims(UUID userId, UUID organizationId, String email, String fullName, Set<RoleType> roles) {
}
