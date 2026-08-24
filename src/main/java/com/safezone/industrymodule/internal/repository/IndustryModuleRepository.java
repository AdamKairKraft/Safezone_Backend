package com.safezone.industrymodule.internal.repository;

import com.safezone.industrymodule.internal.domain.IndustryModuleEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IndustryModuleRepository extends JpaRepository<IndustryModuleEntity, UUID> {

    Optional<IndustryModuleEntity> findByCode(String code);
}
