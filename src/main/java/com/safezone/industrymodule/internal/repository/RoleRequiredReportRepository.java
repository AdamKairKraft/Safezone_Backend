package com.safezone.industrymodule.internal.repository;

import com.safezone.industrymodule.internal.domain.RoleRequiredReport;
import com.safezone.shared.domain.RoleType;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRequiredReportRepository extends JpaRepository<RoleRequiredReport, UUID> {

    List<RoleRequiredReport> findByIndustryModuleIdAndRoleOrderBySortOrder(UUID industryModuleId, RoleType role);

    List<RoleRequiredReport> findByIndustryModuleId(UUID industryModuleId);
}
