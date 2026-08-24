package com.safezone.compliance.internal.service;

import com.safezone.compliance.internal.domain.ComplianceFrequency;
import com.safezone.compliance.internal.domain.ComplianceRequirement;
import java.util.List;
import java.util.UUID;

public interface ComplianceService {

    ComplianceRequirement create(
            UUID id,
            UUID organizationId,
            UUID siteId,
            String industryModuleCode,
            String categoryCode,
            String reportTypeCode,
            ComplianceFrequency frequency);

    List<ComplianceStatusView> listStatusBySite(UUID siteId);
}
