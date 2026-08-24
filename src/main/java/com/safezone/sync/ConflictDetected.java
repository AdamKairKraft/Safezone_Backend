package com.safezone.sync;

import java.time.Instant;
import java.util.UUID;

/** Published when a push detects a version mismatch. Consumed by the notification module. */
public record ConflictDetected(UUID conflictId, String entityType, UUID entityId, UUID submittedBy, Instant occurredAt) {
}
