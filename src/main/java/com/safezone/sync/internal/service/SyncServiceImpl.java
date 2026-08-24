package com.safezone.sync.internal.service;

import com.safezone.sync.ConflictDetected;
import com.safezone.sync.SyncOutcome;
import com.safezone.sync.SyncRecord;
import com.safezone.sync.SyncStatus;
import com.safezone.sync.internal.domain.SyncConflict;
import com.safezone.sync.internal.repository.SyncConflictRepository;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
class SyncServiceImpl implements SyncService {

    private final SyncableRegistry syncableRegistry;
    private final SyncConflictRepository syncConflictRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public List<SyncMutationResult> push(List<SyncMutationCommand> mutations, UUID actorId) {
        return mutations.stream().map(mutation -> applyMutation(mutation, actorId)).toList();
    }

    private SyncMutationResult applyMutation(SyncMutationCommand mutation, UUID actorId) {
        var syncable = syncableRegistry.get(mutation.entityType());

        SyncOutcome outcome = syncable.upsert(mutation.entityId(), mutation.payload(), mutation.baseVersion(), actorId);

        if (outcome.status() == SyncStatus.APPLIED) {
            return new SyncMutationResult(
                    mutation.entityType(), mutation.entityId(), SyncStatus.APPLIED, outcome.currentVersion(), outcome.currentPayload(), null);
        }

        var conflict = syncConflictRepository.save(new SyncConflict(
                UUID.randomUUID(),
                mutation.entityType(),
                mutation.entityId(),
                mutation.baseVersion() == null ? 0 : mutation.baseVersion(),
                outcome.previousPayload(),
                outcome.currentPayload(),
                actorId));

        eventPublisher.publishEvent(
                new ConflictDetected(conflict.getId(), mutation.entityType(), mutation.entityId(), actorId, Instant.now()));

        return new SyncMutationResult(
                mutation.entityType(),
                mutation.entityId(),
                SyncStatus.CONFLICT,
                outcome.currentVersion(),
                outcome.currentPayload(),
                conflict.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, List<SyncRecord>> pull(Instant since, UUID organizationId) {
        return syncableRegistry.all().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().findChangedSince(since, organizationId)));
    }
}
