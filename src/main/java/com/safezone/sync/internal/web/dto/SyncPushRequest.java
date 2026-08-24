package com.safezone.sync.internal.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

/**
 * The client queues mutations locally while offline and pushes the whole batch in one
 * round trip on reconnect, rather than one request per mutation.
 */
public record SyncPushRequest(@NotEmpty @Valid List<SyncMutationRequest> mutations) {
}
