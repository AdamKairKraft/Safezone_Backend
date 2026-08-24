package com.safezone.sync.internal.service;

import tools.jackson.databind.JsonNode;
import com.safezone.sync.SyncStatus;
import java.util.UUID;

public record SyncMutationResult(
        String entityType, UUID entityId, SyncStatus status, long currentVersion, JsonNode currentPayload, UUID conflictId) {
}
