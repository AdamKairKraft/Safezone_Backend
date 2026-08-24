package com.safezone.industrymodule.internal.web.dto;

import com.safezone.industrymodule.internal.domain.IndustryModuleEntity;
import java.util.UUID;

public record IndustryModuleResponse(UUID id, String code, String name, String description) {

    public static IndustryModuleResponse from(IndustryModuleEntity entity) {
        return new IndustryModuleResponse(entity.getId(), entity.getCode(), entity.getName(), entity.getDescription());
    }
}
