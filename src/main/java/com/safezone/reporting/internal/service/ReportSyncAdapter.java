package com.safezone.reporting.internal.service;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import com.safezone.reporting.internal.domain.Report;
import com.safezone.reporting.internal.repository.ReportRepository;
import com.safezone.sync.SyncOutcome;
import com.safezone.sync.SyncRecord;
import com.safezone.sync.Syncable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/** Plugs the reporting module into sync push/pull without sync depending on Report directly. */
@Component
@RequiredArgsConstructor
@Transactional
class ReportSyncAdapter implements Syncable {

    private final ReportRepository reportRepository;
    private final ObjectMapper objectMapper;

    @Override
    public String entityType() {
        return "report";
    }

    @Override
    public SyncOutcome upsert(UUID entityId, JsonNode payload, Long baseVersion, UUID actorId) {
        var parsed = objectMapper.convertValue(payload, ReportSyncPayload.class);
        var existing = reportRepository.findById(entityId);

        if (existing.isEmpty()) {
            var created = reportRepository.saveAndFlush(new Report(
                    entityId,
                    parsed.organizationId(),
                    parsed.siteId(),
                    parsed.industryModuleCode(),
                    parsed.reportTypeCode(),
                    parsed.data(),
                    parsed.clientCreatedAt()));
            return SyncOutcome.applied(created.getVersion(), toJson(created));
        }

        var report = existing.get();
        boolean isConflict = baseVersion != null && !baseVersion.equals(report.getVersion());
        JsonNode previousJson = isConflict ? toJson(report) : null;

        report.setSiteId(parsed.siteId());
        report.setIndustryModuleCode(parsed.industryModuleCode());
        report.setReportTypeCode(parsed.reportTypeCode());
        report.setData(parsed.data());
        report.setClientCreatedAt(parsed.clientCreatedAt());
        var saved = reportRepository.saveAndFlush(report);

        JsonNode currentJson = toJson(saved);
        return isConflict
                ? SyncOutcome.conflict(saved.getVersion(), currentJson, previousJson)
                : SyncOutcome.applied(saved.getVersion(), currentJson);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SyncRecord> findChangedSince(Instant since, UUID organizationId) {
        return reportRepository.findByOrganizationIdAndUpdatedAtAfter(organizationId, since).stream()
                .map(report -> new SyncRecord(report.getId(), report.getVersion(), toJson(report), report.getUpdatedAt()))
                .toList();
    }

    private JsonNode toJson(Report report) {
        return objectMapper.valueToTree(ReportSyncView.from(report));
    }
}
