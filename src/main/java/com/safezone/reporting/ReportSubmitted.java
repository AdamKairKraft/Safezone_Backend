package com.safezone.reporting;

import java.time.Instant;
import java.util.UUID;

/** Published when a report moves DRAFT -> SUBMITTED. Consumed by notification and compliance. */
public record ReportSubmitted(
        UUID reportId, UUID organizationId, UUID siteId, String reportTypeCode, UUID submittedBy, Instant occurredAt) {
}
