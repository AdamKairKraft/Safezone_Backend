package com.safezone.sync.internal.service;

import tools.jackson.databind.JsonNode;
import java.util.UUID;

public record SyncMutationCommand(String entityType, UUID entityId, JsonNode payload, Long baseVersion) {
}
