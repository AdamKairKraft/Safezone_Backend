package com.safezone.compliance.internal.repository;

import com.safezone.compliance.internal.domain.ComplianceRequirement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplianceRequirementRepository extends JpaRepository<ComplianceRequirement, UUID> {

    List<ComplianceRequirement> findBySiteId(UUID siteId);

    Optional<ComplianceRequirement> findBySiteIdAndReportTypeCode(UUID siteId, String reportTypeCode);
}
