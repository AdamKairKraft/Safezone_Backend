package com.safezone.shared.idempotency;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class IdempotencyServiceImpl implements IdempotencyService {

    private final IdempotencyRecordRepository repository;

    @Override
    public Optional<IdempotencyRecord> find(String idempotencyKey) {
        return repository.findByIdempotencyKey(idempotencyKey);
    }

    @Override
    public IdempotencyRecord save(String idempotencyKey, String requestHash, int responseStatus, String responseBody) {
        return repository.save(new IdempotencyRecord(idempotencyKey, requestHash, responseStatus, responseBody));
    }
}
