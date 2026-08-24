package com.safezone.sync.internal.web.dto;

import tools.jackson.databind.JsonNode;
import com.safezone.sync.internal.domain.ConflictStatus;
import com.safezone.sync.internal.domain.SyncConflict;
import java.time.Instant;
import java.util.UUID;

public record ConflictResponse(
        UUID id,
        String entityType,
        UUID entityId,
        JsonNode losingPayload,
        JsonNode winningPayload,
        UUID submittedBy,
        Instant submittedAt,
        ConflictStatus status) {

    public static ConflictResponse from(SyncConflict conflict) {
        return new ConflictResponse(
                conflict.getId(),
                conflict.getEntityType(),
                conflict.getEntityId(),
                conflict.getLosingPayload(),
                conflict.getWinningPayload(),
                conflict.getSubmittedBy(),
                conflict.getSubmittedAt(),
                conflict.getStatus());
    }
}
