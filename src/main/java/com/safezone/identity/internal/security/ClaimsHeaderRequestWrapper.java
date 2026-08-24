package com.safezone.identity.internal.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

/**
 * Overrides X-User-Id / X-Organization-Id to the verified access token's claims,
 * regardless of what the client itself sent for those headers. This lets every existing
 * {@code @RequestHeader("X-User-Id"/"X-Organization-Id")} controller (sync, reports,
 * conflicts, ...) keep its exact signature - those values just become cryptographically
 * trustworthy instead of client-asserted.
 */
class ClaimsHeaderRequestWrapper extends HttpServletRequestWrapper {

    private final Map<String, String> overrides;

    ClaimsHeaderRequestWrapper(HttpServletRequest request, AccessTokenClaims claims) {
        super(request);
        this.overrides = Map.of(
                "X-User-Id", claims.userId().toString(),
                "X-Organization-Id", claims.organizationId().toString());
    }

    @Override
    public String getHeader(String name) {
        return findOverride(name).orElseGet(() -> super.getHeader(name));
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        return findOverride(name)
                .map(value -> Collections.enumeration(List.of(value)))
                .orElseGet(() -> super.getHeaders(name));
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        var names = new LinkedHashSet<>(Collections.list(super.getHeaderNames()));
        names.addAll(overrides.keySet());
        return Collections.enumeration(names);
    }

    private java.util.Optional<String> findOverride(String name) {
        return overrides.entrySet().stream()
                .filter(entry -> entry.getKey().equalsIgnoreCase(name))
                .map(Map.Entry::getValue)
                .findFirst();
    }
}
