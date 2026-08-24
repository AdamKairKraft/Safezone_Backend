package com.safezone.shefiles.internal.web;

import com.safezone.shefiles.internal.service.SheFileService;
import com.safezone.shefiles.internal.web.dto.RegisterSheFileRequest;
import com.safezone.shefiles.internal.web.dto.SheFileResponse;
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
@RequestMapping("/api/she-files")
@RequiredArgsConstructor
class SheFileController {

    private final SheFileService sheFileService;

    @PostMapping
    public ResponseEntity<SheFileResponse> register(@Valid @RequestBody RegisterSheFileRequest request) {
        var file = sheFileService.register(
                request.id(),
                request.organizationId(),
                request.siteId(),
                request.category(),
                request.title(),
                request.ownerUserId(),
                request.storageKey(),
                request.expiryDate());
        return ResponseEntity.created(URI.create("/api/she-files/" + file.getId())).body(SheFileResponse.from(file));
    }

    @GetMapping("/{id}")
    public SheFileResponse get(@PathVariable UUID id) {
        return SheFileResponse.from(sheFileService.get(id));
    }

    @GetMapping
    public List<SheFileResponse> listByOrganization(@RequestParam UUID organizationId) {
        return sheFileService.listByOrganization(organizationId).stream().map(SheFileResponse::from).toList();
    }
}
