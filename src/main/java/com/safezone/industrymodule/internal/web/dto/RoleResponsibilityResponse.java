package com.safezone.industrymodule.internal.web.dto;

import com.safezone.industrymodule.internal.domain.RoleResponsibility;
import java.util.UUID;

public record RoleResponsibilityResponse(UUID id, String description) {

    public static RoleResponsibilityResponse from(RoleResponsibility responsibility) {
        return new RoleResponsibilityResponse(responsibility.getId(), responsibility.getDescription());
    }
}
