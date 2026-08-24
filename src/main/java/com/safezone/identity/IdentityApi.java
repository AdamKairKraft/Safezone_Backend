package com.safezone.identity;

import java.util.Optional;
import java.util.UUID;

/**
 * Public surface of the identity module. Other modules (reporting, compliance,
 * shefiles, sync) depend on this interface only — never on identity's internal
 * entities or repositories.
 */
public interface IdentityApi {

    Optional<OrganizationRef> findOrganization(UUID organizationId);

    Optional<SiteRef> findSite(UUID siteId);

    boolean siteBelongsToOrganization(UUID siteId, UUID organizationId);
}
