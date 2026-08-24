package com.safezone.reporting.internal.domain;

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

/**
 * One row per report of any type across every industry module. Fields common to all
 * report types (routing, status, timestamps) are real columns for querying; the
 * type-specific fields defined by the report's form schema live in {@code data}.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "reports")
public class Report extends BaseEntity {

    @Column(name = "organization_id", nullable = false)
    private UUID organizationId;

    @Column(name = "site_id", nullable = false)
    private UUID siteId;

    @Column(name = "industry_module_code", nullable = false)
    private String industryModuleCode;

    @Column(name = "report_type_code", nullable = false)
    private String reportTypeCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportStatus status;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false)
    private JsonNode data;

    @Column(name = "submitted_by")
    private UUID submittedBy;

    @Column(name = "client_created_at")
    private Instant clientCreatedAt;

    @Column(name = "server_received_at", nullable = false)
    private Instant serverReceivedAt;

    public Report(
            UUID id,
            UUID organizationId,
            UUID siteId,
            String industryModuleCode,
            String reportTypeCode,
            JsonNode data,
            Instant clientCreatedAt) {
        super(id);
        this.organizationId = organizationId;
        this.siteId = siteId;
        this.industryModuleCode = industryModuleCode;
        this.reportTypeCode = reportTypeCode;
        this.status = ReportStatus.DRAFT;
        this.data = data;
        this.clientCreatedAt = clientCreatedAt;
        this.serverReceivedAt = Instant.now();
    }
}
