package com.safezone.sync.internal.service;

import tools.jackson.databind.JsonNode;
import com.safezone.shared.web.NotFoundException;
import com.safezone.sync.internal.domain.ConflictResolution;
import com.safezone.sync.internal.domain.ConflictStatus;
import com.safezone.sync.internal.domain.SyncConflict;
import com.safezone.sync.internal.repository.SyncConflictRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
class ConflictServiceImpl implements ConflictService {

    private final SyncConflictRepository syncConflictRepository;
    private final SyncableRegistry syncableRegistry;

    @Override
    @Transactional(readOnly = true)
    public List<SyncConflict> listPending() {
        return syncConflictRepository.findByStatus(ConflictStatus.PENDING);
    }

    @Override
    public SyncConflict resolve(UUID conflictId, ConflictResolution resolution, JsonNode mergedPayload, UUID resolvedBy) {
        var conflict = syncConflictRepository.findById(conflictId)
                .orElseThrow(() -> new NotFoundException("Conflict not found: " + conflictId));

        if (conflict.getStatus() == ConflictStatus.RESOLVED) {
            return conflict;
        }

        if (resolution == ConflictResolution.RESTORED_MINE || resolution == ConflictResolution.MERGED) {
            var syncable = syncableRegistry.get(conflict.getEntityType());
            JsonNode payloadToApply = resolution == ConflictResolution.MERGED ? mergedPayload : conflict.getLosingPayload();
            syncable.upsert(conflict.getEntityId(), payloadToApply, null, resolvedBy);
        }

        conflict.resolve(resolution, resolvedBy);
        return conflict;
    }
}
