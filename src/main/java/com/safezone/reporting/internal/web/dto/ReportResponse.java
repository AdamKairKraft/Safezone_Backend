package com.safezone.reporting.internal.web.dto;

import tools.jackson.databind.JsonNode;
import com.safezone.reporting.internal.domain.Report;
import com.safezone.reporting.internal.domain.ReportStatus;
import java.time.Instant;
import java.util.UUID;

public record ReportResponse(
        UUID id,
        UUID organizationId,
        UUID siteId,
        String industryModuleCode,
        String reportTypeCode,
        ReportStatus status,
        JsonNode data,
        UUID submittedBy,
        Instant clientCreatedAt,
        Instant serverReceivedAt,
        long version) {

    public static ReportResponse from(Report report) {
        return new ReportResponse(
                report.getId(),
                report.getOrganizationId(),
                report.getSiteId(),
                report.getIndustryModuleCode(),
                report.getReportTypeCode(),
                report.getStatus(),
                report.getData(),
                report.getSubmittedBy(),
                report.getClientCreatedAt(),
                report.getServerReceivedAt(),
                report.getVersion());
    }
}
