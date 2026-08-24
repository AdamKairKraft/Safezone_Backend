package com.safezone.reporting.internal.service;

import com.safezone.reporting.ReportingApi;
import com.safezone.reporting.internal.repository.ReportRepository;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
class ReportingApiImpl implements ReportingApi {

    private final ReportRepository reportRepository;

    @Override
    public Optional<Instant> findLastSubmittedAt(UUID siteId, String reportTypeCode) {
        return reportRepository
                .findFirstBySiteIdAndReportTypeCodeOrderByServerReceivedAtDesc(siteId, reportTypeCode)
                .filter(report -> report.getSubmittedBy() != null)
                .map(report -> report.getServerReceivedAt());
    }
}
