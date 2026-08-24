package com.safezone.identity.internal.web.dto;

import com.safezone.identity.internal.domain.Site;
import java.util.UUID;

public record SiteResponse(UUID id, UUID organizationId, String name, long version) {

    public static SiteResponse from(Site site) {
        return new SiteResponse(site.getId(), site.getOrganizationId(), site.getName(), site.getVersion());
    }
}
