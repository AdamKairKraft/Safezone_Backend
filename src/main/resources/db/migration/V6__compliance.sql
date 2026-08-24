create table compliance_requirements (
    id uuid primary key,
    version bigint not null default 0,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    organization_id uuid not null,
    site_id uuid not null,
    industry_module_code varchar(64) not null,
    category_code varchar(64) not null,
    report_type_code varchar(64) not null,
    frequency varchar(32) not null,
    last_completed_at timestamptz
);

create index ix_compliance_requirements_site_id on compliance_requirements (site_id);
create index ix_compliance_requirements_site_id_report_type_code on compliance_requirements (site_id, report_type_code);
