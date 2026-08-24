package com.safezone.reporting.internal.repository;

import com.safezone.reporting.internal.domain.Report;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, UUID> {

    List<Report> findBySiteId(UUID siteId);

    List<Report> findByOrganizationIdAndUpdatedAtAfter(UUID organizationId, Instant since);

    Optional<Report> findFirstBySiteIdAndReportTypeCodeOrderByServerReceivedAtDesc(UUID siteId, String reportTypeCode);
}
