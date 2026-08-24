package com.safezone.reporting.internal.service;

import tools.jackson.databind.JsonNode;
import com.safezone.reporting.internal.domain.Report;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ReportService {

    /** Idempotent create-or-update-while-draft, keyed by client-supplied id. */
    Report upsertDraft(
            UUID id,
            UUID organizationId,
            UUID siteId,
            String industryModuleCode,
            String reportTypeCode,
            JsonNode data,
            Instant clientCreatedAt);

    Report get(UUID id);

    List<Report> listBySite(UUID siteId);

    /** Idempotent: submitting an already-submitted report is a no-op, not an error. */
    Report submit(UUID id, UUID actorId);
}
