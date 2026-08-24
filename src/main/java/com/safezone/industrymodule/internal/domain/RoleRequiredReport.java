package com.safezone.industrymodule.internal.domain;

import com.safezone.shared.domain.BaseEntity;
import com.safezone.shared.domain.RoleType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * {@code reportTypeCode}/{@code reportTypeName} are stored directly rather than joined
 * from {@link ReportTypeDefinition} - not every required report a role has been assigned
 * necessarily has a formSchema-backed report type built yet. Where the code does match a
 * real {@code ReportTypeDefinition.code}, the frontend can still cross-reference live
 * compliance status for it.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "role_required_reports")
public class RoleRequiredReport extends BaseEntity {

    @Column(name = "industry_module_id", nullable = false)
    private UUID industryModuleId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleType role;

    @Column(name = "report_type_code", nullable = false)
    private String reportTypeCode;

    @Column(name = "report_type_name", nullable = false)
    private String reportTypeName;

    @Column(name = "frequency_label", nullable = false)
    private String frequencyLabel;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;
}
