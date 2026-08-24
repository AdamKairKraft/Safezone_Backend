package com.safezone.identity.internal.web;

import com.safezone.identity.internal.service.OrganizationService;
import com.safezone.identity.internal.web.dto.CreateOrganizationRequest;
import com.safezone.identity.internal.web.dto.OrganizationResponse;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@RestController
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
class OrganizationController {

    private final OrganizationService organizationService;

    @PostMapping
    public ResponseEntity<OrganizationResponse> create(@Valid @RequestBody CreateOrganizationRequest request) {
        var organization = organizationService.create(request.id(), request.name());
        return ResponseEntity.created(URI.create("/api/organizations/" + organization.getId()))
                .body(OrganizationResponse.from(organization));
    }

    @GetMapping("/{id}")
    public OrganizationResponse get(@PathVariable UUID id) {
        return OrganizationResponse.from(organizationService.get(id));
    }
}
