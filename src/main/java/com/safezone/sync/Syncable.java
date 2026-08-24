package com.safezone.sync;

import tools.jackson.databind.JsonNode;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Implemented by any domain module whose entities can be created/edited offline and
 * need to flow through sync push/pull. The sync module never depends on a domain
 * module's entities or repositories directly — it discovers all {@code Syncable}
 * beans at startup and dispatches to the one matching {@link #entityType()}, so
 * adding a new syncable entity type never requires a change inside the sync module.
 */
public interface Syncable {

    /** Stable identifier used as the "entityType" in push/pull payloads, e.g. "report". */
    String entityType();

    /**
     * Idempotent upsert by entity id. {@code baseVersion} is what the client believes
     * the current server version is; a mismatch against the actual stored version is
     * a conflict. Implementations must still apply the incoming payload (last-write-wins)
     * even on conflict, and report what was overwritten via {@link SyncOutcome#previousPayload()}.
     */
    SyncOutcome upsert(UUID entityId, JsonNode payload, Long baseVersion, UUID actorId);

    /** Everything of this type, scoped to the organization, changed since {@code since}. */
    List<SyncRecord> findChangedSince(Instant since, UUID organizationId);
}
