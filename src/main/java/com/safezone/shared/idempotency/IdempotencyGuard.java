package com.safezone.shared.idempotency;

import tools.jackson.databind.ObjectMapper;
import com.safezone.shared.web.ConflictException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
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

    // No Class<T>/Supplier<T> (or even raw Class/Supplier) parameters here, on purpose:
    // Spring Modulith's observability tracing reflectively renders every parameter's
    // generic type for each cross-module call, and java.lang.Class/java.util.function.
    // Supplier are themselves declared generic (Class<T>, Supplier<T>) - any reference
    // to them, raw or wildcarded, still reports hasGenerics()=true and recurses into
    // that unresolvable declared type variable, NullPointerExceptions. Only genuinely
    // non-generic types (String, Object) are safe here. Callers do their own
    // (de)serialization with their own ObjectMapper instead of handing this a type
    // token or a callback to invoke.

    /** The previously-saved response JSON for this key, or null if it hasn't been used yet. */
    public String findExistingResponseJson(String idempotencyKey, String requestHash) {
        var existing = idempotencyService.find(idempotencyKey);
        if (existing.isEmpty()) {
            return null;
        }
        if (!existing.get().getRequestHash().equals(requestHash)) {
            throw new ConflictException("Idempotency-Key reused with a different request body");
        }
        return existing.get().getResponseBody();
    }

    public void save(String idempotencyKey, String requestHash, Object result) {
        idempotencyService.save(idempotencyKey, requestHash, 200, writeValue(result));
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
}
