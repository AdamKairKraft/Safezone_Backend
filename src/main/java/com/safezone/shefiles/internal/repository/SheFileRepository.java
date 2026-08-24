package com.safezone.shefiles.internal.repository;

import com.safezone.shefiles.internal.domain.SheFile;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SheFileRepository extends JpaRepository<SheFile, UUID> {

    List<SheFile> findByOrganizationId(UUID organizationId);
}
