package com.safezone.industrymodule.internal.service;

import com.safezone.industrymodule.internal.domain.IndustryModuleEntity;
import com.safezone.industrymodule.internal.domain.ReportTypeDefinition;
import com.safezone.industrymodule.internal.domain.RoleRequiredReport;
import com.safezone.industrymodule.internal.domain.RoleResponsibility;
import com.safezone.shared.domain.RoleType;
import java.util.List;

public interface IndustryModuleService {

    List<IndustryModuleEntity> listModules();

    IndustryModuleEntity getByCode(String code);

    List<ReportTypeDefinition> listReportTypes(String industryModuleCode);

    List<RoleType> listRoles(String industryModuleCode);

    List<RoleResponsibility> listResponsibilities(String industryModuleCode, RoleType role);

    List<RoleRequiredReport> listRequiredReports(String industryModuleCode, RoleType role);
}
