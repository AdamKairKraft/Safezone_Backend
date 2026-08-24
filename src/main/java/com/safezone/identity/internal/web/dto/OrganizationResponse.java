package com.safezone.identity.internal.web.dto;

import com.safezone.identity.internal.domain.Organization;
import java.util.UUID;

public record OrganizationResponse(UUID id, String name, long version) {

    public static OrganizationResponse from(Organization organization) {
        return new OrganizationResponse(organization.getId(), organization.getName(), organization.getVersion());
    }
}
