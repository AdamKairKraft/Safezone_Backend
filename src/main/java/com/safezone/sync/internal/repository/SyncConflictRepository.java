package com.safezone.sync.internal.repository;

import com.safezone.sync.internal.domain.ConflictStatus;
import com.safezone.sync.internal.domain.SyncConflict;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SyncConflictRepository extends JpaRepository<SyncConflict, UUID> {

    List<SyncConflict> findByStatus(ConflictStatus status);
}
