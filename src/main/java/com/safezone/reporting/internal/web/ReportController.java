package com.safezone.reporting.internal.web;

import tools.jackson.databind.ObjectMapper;
import com.safezone.reporting.internal.service.ReportService;
import com.safezone.reporting.internal.web.dto.ReportResponse;
import com.safezone.reporting.internal.web.dto.UpsertReportRequest;
import com.safezone.shared.idempotency.IdempotencyGuard;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
class ReportController {

    private final ReportService reportService;
    private final IdempotencyGuard idempotencyGuard;
    private final ObjectMapper objectMapper;

    // PUT, not POST: the id is client-generated, so create-or-update-while-draft is
    // naturally idempotent by id and doesn't need a separate Idempotency-Key.
    @PutMapping("/{id}")
    public ReportResponse upsert(@PathVariable UUID id, @Valid @RequestBody UpsertReportRequest request) {
        var report = reportService.upsertDraft(
                id,
                request.organizationId(),
                request.siteId(),
                request.industryModuleCode(),
                request.reportTypeCode(),
                request.data(),
                request.clientCreatedAt());
        return ReportResponse.from(report);
    }

    @GetMapping("/{id}")
    public ReportResponse get(@PathVariable UUID id) {
        return ReportResponse.from(reportService.get(id));
    }

    @GetMapping
    public List<ReportResponse> listBySite(@RequestParam UUID siteId) {
        return reportService.listBySite(siteId).stream().map(ReportResponse::from).toList();
    }

    // Submit fires a domain event (notifications etc.) so it isn't naturally idempotent
    // the way the upsert is - guard it against a retried request after a dropped connection.
    @PostMapping("/{id}/submit")
    public ReportResponse submit(
            @PathVariable UUID id,
            @RequestHeader("X-User-Id") UUID actorId,
            @RequestHeader("Idempotency-Key") String idempotencyKey) {
        String requestHash = idempotencyGuard.hash(id, actorId);
        String existingJson = idempotencyGuard.findExistingResponseJson(idempotencyKey, requestHash);
        if (existingJson != null) {
            return objectMapper.readValue(existingJson, ReportResponse.class);
        }
        var response = ReportResponse.from(reportService.submit(id, actorId));
        idempotencyGuard.save(idempotencyKey, requestHash, response);
        return response;
    }
}
