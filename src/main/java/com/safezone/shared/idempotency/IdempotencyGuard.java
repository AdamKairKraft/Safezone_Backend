package com.safezone.shared.idempotency;

import tools.jackson.databind.ObjectMapper;
import com.safezone.shared.web.ConflictException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Wraps a non-naturally-idempotent action (a status transition, a conflict resolution)
 * so that a sync retry replaying the same request after a dropped connection returns
 * the original result instead of re-executing the side effect.
 */
@Component
@RequiredArgsConstructor
public class IdempotencyGuard {

    private final IdempotencyService idempotencyService;
    private final ObjectMapper objectMapper;

    public <T> T execute(String idempotencyKey, String requestHash, Class<T> responseType, Supplier<T> action) {
        var existing = idempotencyService.find(idempotencyKey);
        if (existing.isPresent()) {
            if (!existing.get().getRequestHash().equals(requestHash)) {
                throw new ConflictException("Idempotency-Key reused with a different request body");
            }
            return readValue(existing.get().getResponseBody(), responseType);
        }
        T result = action.get();
        idempotencyService.save(idempotencyKey, requestHash, 200, writeValue(result));
        return result;
    }

    public String hash(Object... parts) {
        var joined = new StringBuilder();
        for (Object part : parts) {
            joined.append(part).append('|');
        }
        try {
            var digest = MessageDigest.getInstance("SHA-256").digest(joined.toString().getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    private String writeValue(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private <T> T readValue(String json, Class<T> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
