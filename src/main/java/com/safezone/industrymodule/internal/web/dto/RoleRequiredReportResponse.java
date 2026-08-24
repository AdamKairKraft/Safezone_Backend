package com.safezone.industrymodule.internal.web.dto;

import com.safezone.industrymodule.internal.domain.RoleRequiredReport;
import java.util.UUID;

public record RoleRequiredReportResponse(UUID id, String reportTypeCode, String reportTypeName, String frequencyLabel) {

    public static RoleRequiredReportResponse from(RoleRequiredReport requiredReport) {
        return new RoleRequiredReportResponse(
                requiredReport.getId(),
                requiredReport.getReportTypeCode(),
                requiredReport.getReportTypeName(),
                requiredReport.getFrequencyLabel());
    }
}
