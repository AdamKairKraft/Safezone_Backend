package com.safezone.identity;

import java.util.UUID;

/** Read-only projection of an Organization, safe for other modules to depend on. */
public record OrganizationRef(UUID id, String name) {
}
