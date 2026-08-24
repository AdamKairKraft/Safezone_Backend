create table reports (
    id uuid primary key,
    version bigint not null default 0,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    organization_id uuid not null,
    site_id uuid not null,
    industry_module_code varchar(64) not null,
    report_type_code varchar(64) not null,
    status varchar(32) not null,
    data jsonb not null,
    submitted_by uuid,
    client_created_at timestamptz,
    server_received_at timestamptz not null default now()
);

create index ix_reports_site_id on reports (site_id);
create index ix_reports_organization_id_updated_at on reports (organization_id, updated_at);
create index ix_reports_site_id_report_type_code on reports (site_id, report_type_code, server_received_at desc);
