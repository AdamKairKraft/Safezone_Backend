package com.safezone.industrymodule;

import tools.jackson.databind.JsonNode;
import java.util.UUID;

/** {@code formSchema} is the field definition the client renders the dynamic report form from. */
public record ReportTypeDefinitionRef(UUID id, String industryModuleCode, String code, String name, JsonNode formSchema) {
}
