package com.safezone.sync.internal.web;

import com.safezone.shared.idempotency.IdempotencyGuard;
import com.safezone.sync.internal.service.ConflictService;
import com.safezone.sync.internal.web.dto.ConflictResponse;
import com.safezone.sync.internal.web.dto.ResolveConflictRequest;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/conflicts")
@RequiredArgsConstructor
class ConflictController {

    private final ConflictService conflictService;
    private final IdempotencyGuard idempotencyGuard;

    @GetMapping
    public List<ConflictResponse> listPending() {
        return conflictService.listPending().stream().map(ConflictResponse::from).toList();
    }

    // Guarded by Idempotency-Key: a resolve retried after a dropped connection must
    // not re-apply the resolution (e.g. re-write "restored mine") a second time.
    @PostMapping("/{id}/resolve")
    public ConflictResponse resolve(
            @PathVariable UUID id,
            @RequestHeader("X-User-Id") UUID actorId,
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @Valid @RequestBody ResolveConflictRequest request) {
        String requestHash = idempotencyGuard.hash(id, request.resolution(), request.mergedPayload());
        return idempotencyGuard.execute(
                idempotencyKey,
                requestHash,
                ConflictResponse.class,
                () -> ConflictResponse.from(conflictService.resolve(id, request.resolution(), request.mergedPayload(), actorId)));
    }
}
