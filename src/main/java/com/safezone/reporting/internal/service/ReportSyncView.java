package com.safezone.reporting.internal.service;

import tools.jackson.databind.JsonNode;
import com.safezone.reporting.internal.domain.Report;
import com.safezone.reporting.internal.domain.ReportStatus;
import java.time.Instant;
import java.util.UUID;

/** JSON projection of a Report used as the sync {@code currentPayload}/{@code previousPayload}. */
record ReportSyncView(
        UUID organizationId,
        UUID siteId,
        String industryModuleCode,
        String reportTypeCode,
        ReportStatus status,
        JsonNode data,
        Instant clientCreatedAt,
        UUID submittedBy) {

    static ReportSyncView from(Report report) {
        return new ReportSyncView(
                report.getOrganizationId(),
                report.getSiteId(),
                report.getIndustryModuleCode(),
                report.getReportTypeCode(),
                report.getStatus(),
                report.getData(),
                report.getClientCreatedAt(),
                report.getSubmittedBy());
    }
}
