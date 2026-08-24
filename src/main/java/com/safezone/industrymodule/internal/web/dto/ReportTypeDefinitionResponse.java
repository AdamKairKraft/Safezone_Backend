package com.safezone.industrymodule.internal.web.dto;

import tools.jackson.databind.JsonNode;
import com.safezone.industrymodule.internal.domain.ReportTypeDefinition;
import java.util.UUID;

public record ReportTypeDefinitionResponse(UUID id, String code, String name, JsonNode formSchema) {

    public static ReportTypeDefinitionResponse from(ReportTypeDefinition definition) {
        return new ReportTypeDefinitionResponse(
                definition.getId(), definition.getCode(), definition.getName(), definition.getFormSchema());
    }
}
