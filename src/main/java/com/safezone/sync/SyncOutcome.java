package com.safezone.sync;

import tools.jackson.databind.JsonNode;

/**
 * Result of a {@link Syncable#upsert} call. On CONFLICT, {@code previousPayload}
 * is the server-side value that just got overwritten by last-write-wins, kept so
 * the caller can snapshot it for later manual resolution.
 */
public record SyncOutcome(SyncStatus status, long currentVersion, JsonNode currentPayload, JsonNode previousPayload) {

    public static SyncOutcome applied(long version, JsonNode payload) {
        return new SyncOutcome(SyncStatus.APPLIED, version, payload, null);
    }

    public static SyncOutcome conflict(long version, JsonNode payload, JsonNode previousPayload) {
        return new SyncOutcome(SyncStatus.CONFLICT, version, payload, previousPayload);
    }
}
