package com.safezone.compliance.internal.service;

import com.safezone.compliance.internal.domain.ComplianceFrequency;
import com.safezone.compliance.internal.domain.ComplianceRequirement;
import com.safezone.compliance.internal.repository.ComplianceRequirementRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
class ComplianceServiceImpl implements ComplianceService {

    private final ComplianceRequirementRepository complianceRequirementRepository;

    @Override
    public ComplianceRequirement create(
            UUID id,
            UUID organizationId,
            UUID siteId,
            String industryModuleCode,
            String categoryCode,
            String reportTypeCode,
            ComplianceFrequency frequency) {
        return complianceRequirementRepository.findById(id)
                .orElseGet(() -> complianceRequirementRepository.save(new ComplianceRequirement(
                        id, organizationId, siteId, industryModuleCode, categoryCode, reportTypeCode, frequency)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComplianceStatusView> listStatusBySite(UUID siteId) {
        return complianceRequirementRepository.findBySiteId(siteId).stream().map(ComplianceStatusView::of).toList();
    }
}
