package com.safezone.shefiles.internal.service;

import com.safezone.shared.web.NotFoundException;
import com.safezone.shefiles.internal.domain.SheFile;
import com.safezone.shefiles.internal.domain.SheFileCategory;
import com.safezone.shefiles.internal.repository.SheFileRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
class SheFileServiceImpl implements SheFileService {

    private final SheFileRepository sheFileRepository;

    @Override
    public SheFile register(
            UUID id,
            UUID organizationId,
            UUID siteId,
            SheFileCategory category,
            String title,
            UUID ownerUserId,
            String storageKey,
            LocalDate expiryDate) {
        return sheFileRepository.findById(id)
                .orElseGet(() -> sheFileRepository.save(
                        new SheFile(id, organizationId, siteId, category, title, ownerUserId, storageKey, expiryDate)));
    }

    @Override
    @Transactional(readOnly = true)
    public SheFile get(UUID id) {
        return sheFileRepository.findById(id).orElseThrow(() -> new NotFoundException("SHE file not found: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SheFile> listByOrganization(UUID organizationId) {
        return sheFileRepository.findByOrganizationId(organizationId);
    }
}
