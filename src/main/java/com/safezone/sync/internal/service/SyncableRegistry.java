package com.safezone.sync.internal.service;

import com.safezone.sync.Syncable;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class SyncableRegistry {

    private final List<Syncable> syncables;
    private Map<String, Syncable> byEntityType;

    @PostConstruct
    void init() {
        byEntityType = syncables.stream().collect(Collectors.toMap(Syncable::entityType, Function.identity()));
    }

    Syncable get(String entityType) {
        var syncable = byEntityType.get(entityType);
        if (syncable == null) {
            throw new IllegalArgumentException("Unknown syncable entity type: " + entityType);
        }
        return syncable;
    }

    Map<String, Syncable> all() {
        return byEntityType;
    }
}
