package com.safezone.shefiles.internal.service;

import com.safezone.shefiles.internal.domain.SheFile;
import com.safezone.shefiles.internal.domain.SheFileCategory;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface SheFileService {

    SheFile register(
            UUID id,
            UUID organizationId,
            UUID siteId,
            SheFileCategory category,
            String title,
            UUID ownerUserId,
            String storageKey,
            LocalDate expiryDate);

    SheFile get(UUID id);

    List<SheFile> listByOrganization(UUID organizationId);
}
