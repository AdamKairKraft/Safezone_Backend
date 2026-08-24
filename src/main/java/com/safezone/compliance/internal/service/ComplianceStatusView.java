package com.safezone.compliance.internal.service;

import com.safezone.compliance.internal.domain.ComplianceRequirement;
import com.safezone.compliance.internal.domain.ComplianceState;

public record ComplianceStatusView(ComplianceRequirement requirement, ComplianceState state) {

    public static ComplianceStatusView of(ComplianceRequirement requirement) {
        return new ComplianceStatusView(requirement, requirement.deriveState());
    }
}
