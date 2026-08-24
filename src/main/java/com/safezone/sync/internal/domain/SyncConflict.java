package com.safezone.sync.internal.domain;

import tools.jackson.databind.JsonNode;
import com.safezone.shared.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "sync_conflicts")
public class SyncConflict extends BaseEntity {

    @Column(name = "entity_type", nullable = false)
    private String entityType;

    @Column(name = "entity_id", nullable = false)
    private UUID entityId;

    @Column(name = "base_version", nullable = false)
    private long baseVersion;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "losing_payload", nullable = false)
    private JsonNode losingPayload;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "winning_payload", nullable = false)
    private JsonNode winningPayload;

    @Column(name = "submitted_by")
    private UUID submittedBy;

    @Column(name = "submitted_at", nullable = false)
    private Instant submittedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConflictStatus status;

    @Enumerated(EnumType.STRING)
    private ConflictResolution resolution;

    @Column(name = "resolved_by")
    private UUID resolvedBy;

    @Column(name = "resolved_at")
    private Instant resolvedAt;

    public SyncConflict(
            UUID id,
            String entityType,
            UUID entityId,
            long baseVersion,
            JsonNode losingPayload,
            JsonNode winningPayload,
            UUID submittedBy) {
        super(id);
        this.entityType = entityType;
        this.entityId = entityId;
        this.baseVersion = baseVersion;
        this.losingPayload = losingPayload;
        this.winningPayload = winningPayload;
        this.submittedBy = submittedBy;
        this.submittedAt = Instant.now();
        this.status = ConflictStatus.PENDING;
    }

    public void resolve(ConflictResolution resolution, UUID resolvedBy) {
        this.resolution = resolution;
        this.resolvedBy = resolvedBy;
        this.resolvedAt = Instant.now();
        this.status = ConflictStatus.RESOLVED;
    }
}
