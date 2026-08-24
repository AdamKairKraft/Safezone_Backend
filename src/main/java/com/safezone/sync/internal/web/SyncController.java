package com.safezone.sync.internal.web;

import com.safezone.sync.SyncRecord;
import com.safezone.sync.internal.service.SyncMutationCommand;
import com.safezone.sync.internal.service.SyncService;
import com.safezone.sync.internal.web.dto.SyncMutationResponse;
import com.safezone.sync.internal.web.dto.SyncPushRequest;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// X-User-Id / X-Organization-Id stand in for authenticated principal claims until
// Spring Security is wired in; every sync call is scoped by them either way.
@RestController
@RequestMapping("/api/sync")
@RequiredArgsConstructor
class SyncController {

    private final SyncService syncService;

    @PostMapping("/push")
    public List<SyncMutationResponse> push(
            @RequestHeader("X-User-Id") UUID actorId, @Valid @RequestBody SyncPushRequest request) {
        var commands = request.mutations().stream()
                .map(m -> new SyncMutationCommand(m.entityType(), m.entityId(), m.payload(), m.baseVersion()))
                .toList();
        return syncService.push(commands, actorId).stream().map(SyncMutationResponse::from).toList();
    }

    @GetMapping("/pull")
    public Map<String, List<SyncRecord>> pull(
            @RequestHeader("X-Organization-Id") UUID organizationId,
            @RequestParam(required = false) Instant since) {
        return syncService.pull(since == null ? Instant.EPOCH : since, organizationId);
    }
}
