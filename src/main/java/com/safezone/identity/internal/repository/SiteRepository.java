package com.safezone.identity.internal.repository;

import com.safezone.identity.internal.domain.Site;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteRepository extends JpaRepository<Site, UUID> {

    List<Site> findByOrganizationId(UUID organizationId);
}
