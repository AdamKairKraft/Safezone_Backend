package com.safezone.industrymodule;

import java.util.List;
import java.util.Optional;

/**
 * Public surface of the industrymodule catalog. Report types, compliance categories
 * and terminology all vary per safety domain (construction, food safety, aviation,
 * fire safety, first aid) and are looked up here rather than hardcoded by consumers.
 */
public interface IndustryModuleApi {

    List<IndustryModuleRef> listModules();

    Optional<ReportTypeDefinitionRef> findReportTypeDefinition(String industryModuleCode, String reportTypeCode);

    List<ReportTypeDefinitionRef> listReportTypeDefinitions(String industryModuleCode);
}
