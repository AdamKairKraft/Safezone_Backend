package com.safezone.identity.internal.service;

import com.safezone.identity.IdentityApi;
import com.safezone.identity.OrganizationRef;
import com.safezone.identity.SiteRef;
import com.safezone.identity.internal.domain.Site;
import com.safezone.identity.internal.repository.OrganizationRepository;
import com.safezone.identity.internal.repository.SiteRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
class IdentityApiImpl implements IdentityApi {

    private final OrganizationRepository organizationRepository;
    private final SiteRepository siteRepository;

    @Override
    public Optional<OrganizationRef> findOrganization(UUID organizationId) {
        return organizationRepository.findById(organizationId)
                .map(org -> new OrganizationRef(org.getId(), org.getName()));
    }

    @Override
    public Optional<SiteRef> findSite(UUID siteId) {
        return siteRepository.findById(siteId)
                .map(site -> new SiteRef(site.getId(), site.getOrganizationId(), site.getName()));
    }

    @Override
    public boolean siteBelongsToOrganization(UUID siteId, UUID organizationId) {
        return siteRepository.findById(siteId)
                .map(Site::getOrganizationId)
                .map(organizationId::equals)
                .orElse(false);
    }
}
