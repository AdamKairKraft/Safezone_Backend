package com.safezone.reporting;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface ReportingApi {

    /** Used by compliance to derive "last completed" for a recurring requirement. */
    Optional<Instant> findLastSubmittedAt(UUID siteId, String reportTypeCode);
}
