package com.safezone.industrymodule.internal.web;

import com.safezone.industrymodule.internal.service.IndustryModuleService;
import com.safezone.industrymodule.internal.web.dto.IndustryModuleResponse;
import com.safezone.industrymodule.internal.web.dto.ReportTypeDefinitionResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/industry-modules")
@RequiredArgsConstructor
class IndustryModuleController {

    private final IndustryModuleService industryModuleService;

    @GetMapping
    public List<IndustryModuleResponse> list() {
        return industryModuleService.listModules().stream().map(IndustryModuleResponse::from).toList();
    }

    @GetMapping("/{code}/report-types")
    public List<ReportTypeDefinitionResponse> listReportTypes(@PathVariable String code) {
        return industryModuleService.listReportTypes(code).stream().map(ReportTypeDefinitionResponse::from).toList();
    }
}
