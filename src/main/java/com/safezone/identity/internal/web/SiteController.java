package com.safezone.identity.internal.web;

import com.safezone.identity.internal.service.SiteService;
import com.safezone.identity.internal.web.dto.CreateSiteRequest;
import com.safezone.identity.internal.web.dto.SiteResponse;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sites")
@RequiredArgsConstructor
class SiteController {

    private final SiteService siteService;

    @PostMapping
    public ResponseEntity<SiteResponse> create(@Valid @RequestBody CreateSiteRequest request) {
        var site = siteService.create(request.id(), request.organizationId(), request.name());
        return ResponseEntity.created(URI.create("/api/sites/" + site.getId())).body(SiteResponse.from(site));
    }

    @GetMapping("/{id}")
    public SiteResponse get(@PathVariable UUID id) {
        return SiteResponse.from(siteService.get(id));
    }

    @GetMapping
    public List<SiteResponse> listByOrganization(@RequestParam UUID organizationId) {
        return siteService.listByOrganization(organizationId).stream().map(SiteResponse::from).toList();
    }
}
