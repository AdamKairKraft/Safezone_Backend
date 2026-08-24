package com.safezone.identity;

import java.util.UUID;

/** Read-only projection of a Site, safe for other modules to depend on. */
public record SiteRef(UUID id, UUID organizationId, String name) {
}
