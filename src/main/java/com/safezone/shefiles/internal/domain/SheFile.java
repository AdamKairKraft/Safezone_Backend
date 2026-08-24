package com.safezone.shefiles.internal.domain;

import com.safezone.shared.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "she_files")
public class SheFile extends BaseEntity {

    @Column(name = "organization_id", nullable = false)
    private UUID organizationId;

    @Column(name = "site_id")
    private UUID siteId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SheFileCategory category;

    @Column(nullable = false)
    private String title;

    @Column(name = "owner_user_id")
    private UUID ownerUserId;

    // Object storage key (e.g. S3), not the binary itself - the binary is uploaded
    // directly to storage via a presigned URL, not proxied through this service.
    @Column(name = "storage_key", nullable = false)
    private String storageKey;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    public SheFile(
            UUID id,
            UUID organizationId,
            UUID siteId,
            SheFileCategory category,
            String title,
            UUID ownerUserId,
            String storageKey,
            LocalDate expiryDate) {
        super(id);
        this.organizationId = organizationId;
        this.siteId = siteId;
        this.category = category;
        this.title = title;
        this.ownerUserId = ownerUserId;
        this.storageKey = storageKey;
        this.expiryDate = expiryDate;
    }

    public SheFileStatus status() {
        if (expiryDate == null) {
            return SheFileStatus.VALID;
        }
        var today = LocalDate.now();
        if (expiryDate.isBefore(today)) {
            return SheFileStatus.EXPIRED;
        }
        if (!expiryDate.isAfter(today.plusDays(30))) {
            return SheFileStatus.EXPIRING_SOON;
        }
        return SheFileStatus.VALID;
    }
}
