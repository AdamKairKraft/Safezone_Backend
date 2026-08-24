package com.safezone.industrymodule.internal.service;

import com.safezone.industrymodule.internal.domain.IndustryModuleEntity;
import com.safezone.industrymodule.internal.domain.ReportTypeDefinition;
import com.safezone.industrymodule.internal.domain.RoleRequiredReport;
import com.safezone.industrymodule.internal.domain.RoleResponsibility;
import com.safezone.industrymodule.internal.repository.IndustryModuleRepository;
import com.safezone.industrymodule.internal.repository.ReportTypeDefinitionRepository;
import com.safezone.industrymodule.internal.repository.RoleRequiredReportRepository;
import com.safezone.industrymodule.internal.repository.RoleResponsibilityRepository;
import com.safezone.shared.domain.RoleType;
import com.safezone.shared.web.NotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class IndustryModuleServiceImpl implements IndustryModuleService {

    private final IndustryModuleRepository industryModuleRepository;
    private final ReportTypeDefinitionRepository reportTypeDefinitionRepository;
    private final RoleResponsibilityRepository roleResponsibilityRepository;
    private final RoleRequiredReportRepository roleRequiredReportRepository;

    @Override
    public List<IndustryModuleEntity> listModules() {
        return industryModuleRepository.findAll();
    }

    @Override
    public IndustryModuleEntity getByCode(String code) {
        return industryModuleRepository.findByCode(code)
                .orElseThrow(() -> new NotFoundException("Industry module not found: " + code));
    }

    @Override
    public List<ReportTypeDefinition> listReportTypes(String industryModuleCode) {
        var module = getByCode(industryModuleCode);
        return reportTypeDefinitionRepository.findByIndustryModuleId(module.getId());
    }

    @Override
    public List<RoleType> listRoles(String industryModuleCode) {
        var module = getByCode(industryModuleCode);
        return roleResponsibilityRepository.findDistinctRoleByIndustryModuleId(module.getId());
    }

    @Override
    public List<RoleResponsibility> listResponsibilities(String industryModuleCode, RoleType role) {
        var module = getByCode(industryModuleCode);
        return roleResponsibilityRepository.findByIndustryModuleIdAndRoleOrderBySortOrder(module.getId(), role);
    }

    @Override
    public List<RoleRequiredReport> listRequiredReports(String industryModuleCode, RoleType role) {
        var module = getByCode(industryModuleCode);
        return roleRequiredReportRepository.findByIndustryModuleIdAndRoleOrderBySortOrder(module.getId(), role);
    }
}
