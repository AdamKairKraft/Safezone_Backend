package com.safezone.identity.internal.service;

import com.safezone.identity.internal.domain.Site;
import java.util.List;
import java.util.UUID;

public interface SiteService {

    Site create(UUID id, UUID organizationId, String name);

    Site get(UUID id);

    List<Site> listByOrganization(UUID organizationId);
}
