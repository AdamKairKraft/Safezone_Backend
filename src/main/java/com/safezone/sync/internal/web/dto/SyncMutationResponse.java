package com.safezone.sync.internal.web.dto;

import tools.jackson.databind.JsonNode;
import com.safezone.sync.SyncStatus;
import com.safezone.sync.internal.service.SyncMutationResult;
import java.util.UUID;

public record SyncMutationResponse(
        String entityType, UUID entityId, SyncStatus status, long currentVersion, JsonNode currentPayload, UUID conflictId) {

    public static SyncMutationResponse from(SyncMutationResult result) {
        return new SyncMutationResponse(
                result.entityType(), result.entityId(), result.status(), result.currentVersion(), result.currentPayload(), result.conflictId());
    }
}
