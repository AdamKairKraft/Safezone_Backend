package com.safezone.compliance.internal.web;

import com.safezone.compliance.internal.service.ComplianceService;
import com.safezone.compliance.internal.web.dto.ComplianceStatusResponse;
import com.safezone.compliance.internal.web.dto.CreateComplianceRequirementRequest;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@RestController
@RequestMapping("/api/compliance")
@RequiredArgsConstructor
class ComplianceController {

    private final ComplianceService complianceService;

    @PostMapping("/requirements")
    public ResponseEntity<Void> createRequirement(@Valid @RequestBody CreateComplianceRequirementRequest request) {
        var requirement = complianceService.create(
                request.id(),
                request.organizationId(),
                request.siteId(),
                request.industryModuleCode(),
                request.categoryCode(),
                request.reportTypeCode(),
                request.frequency());
        return ResponseEntity.created(URI.create("/api/compliance/requirements/" + requirement.getId())).build();
    }

    @GetMapping("/status")
    public List<ComplianceStatusResponse> statusBySite(@RequestParam UUID siteId) {
        return complianceService.listStatusBySite(siteId).stream().map(ComplianceStatusResponse::from).toList();
    }
}
