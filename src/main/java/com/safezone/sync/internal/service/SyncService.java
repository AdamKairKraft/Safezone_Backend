package com.safezone.sync.internal.service;

import com.safezone.sync.SyncRecord;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface SyncService {

    List<SyncMutationResult> push(List<SyncMutationCommand> mutations, UUID actorId);

    Map<String, List<SyncRecord>> pull(Instant since, UUID organizationId);
}
