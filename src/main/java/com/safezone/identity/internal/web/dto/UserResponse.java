package com.safezone.identity.internal.web.dto;

import com.safezone.identity.internal.domain.AppUser;
import com.safezone.shared.domain.RoleType;
import java.util.Set;
import java.util.UUID;

/** Deliberately omits {@code passwordHash} - never put it anywhere near a response body. */
public record UserResponse(UUID id, UUID organizationId, String email, String fullName, Set<RoleType> roles) {

    public static UserResponse from(AppUser user) {
        return new UserResponse(user.getId(), user.getOrganizationId(), user.getEmail(), user.getFullName(), user.getRoles());
    }
}
