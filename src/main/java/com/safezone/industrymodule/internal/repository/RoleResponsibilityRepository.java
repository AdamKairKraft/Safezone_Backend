package com.safezone.industrymodule.internal.repository;

import com.safezone.industrymodule.internal.domain.RoleResponsibility;
import com.safezone.shared.domain.RoleType;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoleResponsibilityRepository extends JpaRepository<RoleResponsibility, UUID> {

    List<RoleResponsibility> findByIndustryModuleIdAndRoleOrderBySortOrder(UUID industryModuleId, RoleType role);

    List<RoleResponsibility> findByIndustryModuleId(UUID industryModuleId);

    // A derived "findDistinctRoleBy..." can't project a single field this way (Spring
    // Data derived-query naming doesn't support field projection via the method name,
    // only "select distinct <entity>") - needs an explicit projecting query instead.
    @Query("select distinct r.role from RoleResponsibility r where r.industryModuleId = :industryModuleId")
    List<RoleType> findDistinctRoleByIndustryModuleId(@Param("industryModuleId") UUID industryModuleId);
}
