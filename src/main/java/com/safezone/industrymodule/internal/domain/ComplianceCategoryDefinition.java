package com.safezone.industrymodule.internal.domain;

import com.safezone.shared.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "compliance_category_definitions")
public class ComplianceCategoryDefinition extends BaseEntity {

    @Column(name = "industry_module_id", nullable = false)
    private UUID industryModuleId;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    public ComplianceCategoryDefinition(UUID id, UUID industryModuleId, String code, String name) {
        super(id);
        this.industryModuleId = industryModuleId;
        this.code = code;
        this.name = name;
    }
}
