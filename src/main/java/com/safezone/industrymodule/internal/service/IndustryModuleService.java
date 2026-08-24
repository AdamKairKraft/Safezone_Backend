package com.safezone.industrymodule.internal.service;

import com.safezone.industrymodule.internal.domain.IndustryModuleEntity;
import com.safezone.industrymodule.internal.domain.ReportTypeDefinition;
import java.util.List;

public interface IndustryModuleService {

    List<IndustryModuleEntity> listModules();

    IndustryModuleEntity getByCode(String code);

    List<ReportTypeDefinition> listReportTypes(String industryModuleCode);
}
