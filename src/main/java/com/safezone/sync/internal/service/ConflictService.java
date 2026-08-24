package com.safezone.sync.internal.service;

import tools.jackson.databind.JsonNode;
import com.safezone.sync.internal.domain.ConflictResolution;
import com.safezone.sync.internal.domain.SyncConflict;
import java.util.List;
import java.util.UUID;

public interface ConflictService {

    List<SyncConflict> listPending();

    /** {@code mergedPayload} is only used when resolution == MERGED; ignored otherwise. */
    SyncConflict resolve(UUID conflictId, ConflictResolution resolution, JsonNode mergedPayload, UUID resolvedBy);
}
