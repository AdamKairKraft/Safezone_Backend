package com.safezone.reporting.internal.web.dto;

import tools.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

public record UpsertReportRequest(
        @NotNull UUID organizationId,
        @NotNull UUID siteId,
        @NotBlank String industryModuleCode,
        @NotBlank String reportTypeCode,
        @NotNull JsonNode data,
        Instant clientCreatedAt) {
}
