package com.safezone.identity.internal.service;

import com.safezone.identity.internal.domain.Organization;
import java.util.UUID;

public interface OrganizationService {

    Organization create(UUID id, String name);

    Organization get(UUID id);
}
