package com.safezone.shefiles.internal.web.dto;

import com.safezone.shefiles.internal.domain.SheFile;
import com.safezone.shefiles.internal.domain.SheFileCategory;
import com.safezone.shefiles.internal.domain.SheFileStatus;
import java.time.LocalDate;
import java.util.UUID;

public record SheFileResponse(
        UUID id,
        UUID organizationId,
        UUID siteId,
        SheFileCategory category,
        String title,
        UUID ownerUserId,
        LocalDate expiryDate,
        SheFileStatus status) {

    public static SheFileResponse from(SheFile file) {
        return new SheFileResponse(
                file.getId(),
                file.getOrganizationId(),
                file.getSiteId(),
                file.getCategory(),
                file.getTitle(),
                file.getOwnerUserId(),
                file.getExpiryDate(),
                file.status());
    }
}
