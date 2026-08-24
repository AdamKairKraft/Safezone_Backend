package com.safezone.industrymodule.internal.repository;

import com.safezone.industrymodule.internal.domain.ReportTypeDefinition;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportTypeDefinitionRepository extends JpaRepository<ReportTypeDefinition, UUID> {

    Optional<ReportTypeDefinition> findByIndustryModuleIdAndCode(UUID industryModuleId, String code);

    List<ReportTypeDefinition> findByIndustryModuleId(UUID industryModuleId);
}
