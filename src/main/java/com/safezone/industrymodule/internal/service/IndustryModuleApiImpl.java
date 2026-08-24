package com.safezone.industrymodule.internal.service;

import com.safezone.industrymodule.IndustryModuleApi;
import com.safezone.industrymodule.IndustryModuleRef;
import com.safezone.industrymodule.ReportTypeDefinitionRef;
import com.safezone.industrymodule.internal.domain.IndustryModuleEntity;
import com.safezone.industrymodule.internal.repository.IndustryModuleRepository;
import com.safezone.industrymodule.internal.repository.ReportTypeDefinitionRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
class IndustryModuleApiImpl implements IndustryModuleApi {

    private final IndustryModuleRepository industryModuleRepository;
    private final ReportTypeDefinitionRepository reportTypeDefinitionRepository;

    @Override
    public List<IndustryModuleRef> listModules() {
        return industryModuleRepository.findAll().stream().map(this::toRef).toList();
    }

    @Override
    public Optional<ReportTypeDefinitionRef> findReportTypeDefinition(String industryModuleCode, String reportTypeCode) {
        return industryModuleRepository.findByCode(industryModuleCode)
                .flatMap(module -> reportTypeDefinitionRepository
                        .findByIndustryModuleIdAndCode(module.getId(), reportTypeCode)
                        .map(def -> new ReportTypeDefinitionRef(
                                def.getId(), industryModuleCode, def.getCode(), def.getName(), def.getFormSchema())));
    }

    @Override
    public List<ReportTypeDefinitionRef> listReportTypeDefinitions(String industryModuleCode) {
        return industryModuleRepository.findByCode(industryModuleCode)
                .map(module -> reportTypeDefinitionRepository.findByIndustryModuleId(module.getId()).stream()
                        .map(def -> new ReportTypeDefinitionRef(
                                def.getId(), industryModuleCode, def.getCode(), def.getName(), def.getFormSchema()))
                        .toList())
                .orElseGet(List::of);
    }

    private IndustryModuleRef toRef(IndustryModuleEntity entity) {
        return new IndustryModuleRef(entity.getId(), entity.getCode(), entity.getName());
    }
}
