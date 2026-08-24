package com.safezone.sync;

import tools.jackson.databind.JsonNode;
import java.time.Instant;
import java.util.UUID;

/** One entity's current state, as returned by a delta pull. */
public record SyncRecord(UUID id, long version, JsonNode payload, Instant updatedAt) {
}
