package com.safezone.sync.internal.web.dto;

import tools.jackson.databind.JsonNode;
import com.safezone.sync.internal.domain.ConflictResolution;
import jakarta.validation.constraints.NotNull;

/** {@code mergedPayload} is required, and only used, when resolution == MERGED. */
public record ResolveConflictRequest(@NotNull ConflictResolution resolution, JsonNode mergedPayload) {
}
