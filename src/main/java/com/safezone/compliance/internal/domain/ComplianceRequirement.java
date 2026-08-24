package com.safezone.compliance.internal.domain;

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

/**
 * A recurring obligation for a site (e.g. "weekly toolbox talk"). Status is derived
 * on read from {@code frequency} + {@code lastCompletedAt} rather than a maintained
 * task queue - see {@link ComplianceRequirement#deriveState()}.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "compliance_requirements")
public class ComplianceRequirement extends BaseEntity {

    @Column(name = "organization_id", nullable = false)
    private UUID organizationId;

    @Column(name = "site_id", nullable = false)
    private UUID siteId;

    @Column(name = "industry_module_code", nullable = false)
    private String industryModuleCode;

    @Column(name = "category_code", nullable = false)
    private String categoryCode;

    @Column(name = "report_type_code", nullable = false)
    private String reportTypeCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ComplianceFrequency frequency;

    @Column(name = "last_completed_at")
    private Instant lastCompletedAt;

    public ComplianceRequirement(
            UUID id,
            UUID organizationId,
            UUID siteId,
            String industryModuleCode,
            String categoryCode,
            String reportTypeCode,
            ComplianceFrequency frequency) {
        super(id);
        this.organizationId = organizationId;
        this.siteId = siteId;
        this.industryModuleCode = industryModuleCode;
        this.categoryCode = categoryCode;
        this.reportTypeCode = reportTypeCode;
        this.frequency = frequency;
    }

    public ComplianceState deriveState() {
        if (frequency == ComplianceFrequency.AS_NEEDED) {
            return ComplianceState.OK;
        }
        if (lastCompletedAt == null) {
            return ComplianceState.OVERDUE;
        }
        var interval = frequency == ComplianceFrequency.WEEKLY ? java.time.Duration.ofDays(7) : java.time.Duration.ofDays(30);
        var dueSoonWindow = java.time.Duration.ofDays(3);
        var nextDue = lastCompletedAt.plus(interval);
        var now = Instant.now();
        if (now.isAfter(nextDue)) {
            return ComplianceState.OVERDUE;
        }
        if (now.isAfter(nextDue.minus(dueSoonWindow))) {
            return ComplianceState.DUE_SOON;
        }
        return ComplianceState.OK;
    }
}
