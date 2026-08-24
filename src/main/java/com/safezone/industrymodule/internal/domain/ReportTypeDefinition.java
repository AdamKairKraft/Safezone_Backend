package com.safezone.industrymodule.internal.domain;

import tools.jackson.databind.JsonNode;
import com.safezone.shared.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "report_type_definitions")
public class ReportTypeDefinition extends BaseEntity {

    @Column(name = "industry_module_id", nullable = false)
    private UUID industryModuleId;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "form_schema", nullable = false)
    private JsonNode formSchema;

    public ReportTypeDefinition(UUID id, UUID industryModuleId, String code, String name, JsonNode formSchema) {
        super(id);
        this.industryModuleId = industryModuleId;
        this.code = code;
        this.name = name;
        this.formSchema = formSchema;
    }
}
