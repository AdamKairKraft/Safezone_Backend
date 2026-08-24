package com.safezone.compliance.internal.web.dto;

import com.safezone.compliance.internal.domain.ComplianceFrequency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateComplianceRequirementRequest(
        @NotNull UUID id,
        @NotNull UUID organizationId,
        @NotNull UUID siteId,
        @NotBlank String industryModuleCode,
        @NotBlank String categoryCode,
        @NotBlank String reportTypeCode,
        @NotNull ComplianceFrequency frequency) {
}
