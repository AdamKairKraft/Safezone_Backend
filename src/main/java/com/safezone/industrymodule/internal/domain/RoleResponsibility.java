package com.safezone.industrymodule.internal.domain;

import com.safezone.shared.domain.BaseEntity;
import com.safezone.shared.domain.RoleType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "role_responsibilities")
public class RoleResponsibility extends BaseEntity {

    @Column(name = "industry_module_id", nullable = false)
    private UUID industryModuleId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleType role;

    @Column(nullable = false)
    private String description;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;
}
