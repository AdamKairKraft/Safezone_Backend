package com.safezone.compliance.internal.web.dto;

import com.safezone.compliance.internal.domain.ComplianceFrequency;
import com.safezone.compliance.internal.domain.ComplianceState;
import com.safezone.compliance.internal.service.ComplianceStatusView;
import java.time.Instant;
import java.util.UUID;

public record ComplianceStatusResponse(
        UUID id,
        String categoryCode,
        String reportTypeCode,
        ComplianceFrequency frequency,
        Instant lastCompletedAt,
        ComplianceState state) {

    public static ComplianceStatusResponse from(ComplianceStatusView view) {
        var requirement = view.requirement();
        return new ComplianceStatusResponse(
                requirement.getId(),
                requirement.getCategoryCode(),
                requirement.getReportTypeCode(),
                requirement.getFrequency(),
                requirement.getLastCompletedAt(),
                view.state());
    }
}
