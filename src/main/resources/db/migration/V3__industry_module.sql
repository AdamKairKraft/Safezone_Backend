create table industry_modules (
    id uuid primary key,
    version bigint not null default 0,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    code varchar(64) not null,
    name varchar(255) not null,
    description text
);

create unique index ux_industry_modules_code on industry_modules (code);

create table report_type_definitions (
    id uuid primary key,
    version bigint not null default 0,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    industry_module_id uuid not null references industry_modules (id),
    code varchar(64) not null,
    name varchar(255) not null,
    form_schema jsonb not null
);

create unique index ux_report_type_definitions_module_code on report_type_definitions (industry_module_id, code);

create table compliance_category_definitions (
    id uuid primary key,
    version bigint not null default 0,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    industry_module_id uuid not null references industry_modules (id),
    code varchar(64) not null,
    name varchar(255) not null
);

create unique index ux_compliance_category_definitions_module_code on compliance_category_definitions (industry_module_id, code);
