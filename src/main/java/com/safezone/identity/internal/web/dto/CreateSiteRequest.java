package com.safezone.identity.internal.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateSiteRequest(@NotNull UUID id, @NotNull UUID organizationId, @NotBlank String name) {
}
