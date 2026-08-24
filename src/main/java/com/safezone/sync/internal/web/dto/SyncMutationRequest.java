package com.safezone.sync.internal.web.dto;

import tools.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record SyncMutationRequest(
        @NotBlank String entityType, @NotNull UUID entityId, @NotNull JsonNode payload, Long baseVersion) {
}
