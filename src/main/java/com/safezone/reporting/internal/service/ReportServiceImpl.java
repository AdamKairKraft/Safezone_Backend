package com.safezone.reporting.internal.service;

import tools.jackson.databind.JsonNode;
import com.safezone.reporting.ReportSubmitted;
import com.safezone.reporting.internal.domain.Report;
import com.safezone.reporting.internal.domain.ReportStatus;
import com.safezone.reporting.internal.repository.ReportRepository;
import com.safezone.shared.web.ConflictException;
import com.safezone.shared.web.NotFoundException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public Report upsertDraft(
            UUID id,
            UUID organizationId,
            UUID siteId,
            String industryModuleCode,
            String reportTypeCode,
            JsonNode data,
            Instant clientCreatedAt) {

        var existing = reportRepository.findById(id);
        if (existing.isEmpty()) {
            return reportRepository.save(
                    new Report(id, organizationId, siteId, industryModuleCode, reportTypeCode, data, clientCreatedAt));
        }

        var report = existing.get();
        if (report.getStatus() != ReportStatus.DRAFT) {
            throw new ConflictException("Report " + id + " is no longer a draft; edit through sync push instead");
        }
        report.setData(data);
        return report;
    }

    @Override
    @Transactional(readOnly = true)
    public Report get(UUID id) {
        return reportRepository.findById(id).orElseThrow(() -> new NotFoundException("Report not found: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Report> listBySite(UUID siteId) {
        return reportRepository.findBySiteId(siteId);
    }

    @Override
    public Report submit(UUID id, UUID actorId) {
        var report = get(id);
        if (report.getStatus() != ReportStatus.DRAFT) {
            return report;
        }
        report.setStatus(ReportStatus.SUBMITTED);
        report.setSubmittedBy(actorId);
        eventPublisher.publishEvent(new ReportSubmitted(
                report.getId(), report.getOrganizationId(), report.getSiteId(), report.getReportTypeCode(), actorId, Instant.now()));
        return report;
    }
}
