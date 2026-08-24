package com.safezone.reporting.internal.service;

import tools.jackson.databind.JsonNode;
import java.time.Instant;
import java.util.UUID;

/**
 * Wire shape for a report mutation in a sync push/pull. Status is deliberately absent:
 * transitions like "submit" carry business rules (event publication) and only happen
 * through {@link ReportService#submit}, never as a raw field overwrite from sync.
 */
record ReportSyncPayload(
        UUID organizationId, UUID siteId, String industryModuleCode, String reportTypeCode, JsonNode data, Instant clientCreatedAt) {
}
