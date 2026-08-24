package com.safezone.shefiles.internal.web.dto;

import com.safezone.shefiles.internal.domain.SheFileCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

public record RegisterSheFileRequest(
        @NotNull UUID id,
        @NotNull UUID organizationId,
        UUID siteId,
        @NotNull SheFileCategory category,
        @NotBlank String title,
        UUID ownerUserId,
        @NotBlank String storageKey,
        LocalDate expiryDate) {
}
