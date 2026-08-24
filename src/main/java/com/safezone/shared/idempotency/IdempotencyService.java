package com.safezone.shared.idempotency;

import java.util.Optional;

/**
 * Guards non-naturally-idempotent actions (a status transition, a notification-firing
 * step) against duplicate execution when a sync retry replays the same request after
 * a dropped connection. Entity creation itself doesn't need this: upsert-by-UUID
 * already makes it idempotent.
 */
public interface IdempotencyService {

    Optional<IdempotencyRecord> find(String idempotencyKey);

    IdempotencyRecord save(String idempotencyKey, String requestHash, int responseStatus, String responseBody);
}
