package com.safezone.compliance.internal.service;

import com.safezone.compliance.internal.repository.ComplianceRequirementRepository;
import com.safezone.reporting.ReportSubmitted;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Keeps requirement.lastCompletedAt current without reporting knowing compliance
 * exists - the coupling runs one way, through the event only.
 */
@Component
@RequiredArgsConstructor
class ReportSubmissionListener {

    private final ComplianceRequirementRepository complianceRequirementRepository;

    // @ApplicationModuleListener already runs after the publishing transaction commits,
    // so this needs its own new transaction - Spring rejects a plain @Transactional here.
    @ApplicationModuleListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void on(ReportSubmitted event) {
        complianceRequirementRepository
                .findBySiteIdAndReportTypeCode(event.siteId(), event.reportTypeCode())
                .ifPresent(requirement -> requirement.setLastCompletedAt(event.occurredAt()));
    }
}
