package com.safezone.identity.internal.service;

import com.safezone.identity.internal.domain.Organization;
import com.safezone.identity.internal.repository.OrganizationRepository;
import com.safezone.shared.web.NotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;

    @Override
    public Organization create(UUID id, String name) {
        return organizationRepository.findById(id)
                .orElseGet(() -> organizationRepository.save(new Organization(id, name)));
    }

    @Override
    @Transactional(readOnly = true)
    public Organization get(UUID id) {
        return organizationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Organization not found: " + id));
    }
}
