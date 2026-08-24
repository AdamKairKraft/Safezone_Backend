package com.safezone.industrymodule.internal.repository;

import com.safezone.industrymodule.internal.domain.ComplianceCategoryDefinition;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplianceCategoryDefinitionRepository extends JpaRepository<ComplianceCategoryDefinition, UUID> {

    List<ComplianceCategoryDefinition> findByIndustryModuleId(UUID industryModuleId);
}
