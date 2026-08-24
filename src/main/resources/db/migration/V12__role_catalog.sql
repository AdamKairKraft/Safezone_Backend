-- Roles & Responsibilities catalog: which responsibilities and required reports each
-- RoleType has under a given industry module. Versioned (not the local-only demo seed)
-- because this is reference/catalog data, same treatment as V8__seed_industry_modules.sql.
--
-- report_type_name/frequency_label are stored directly rather than joined from
-- report_type_definitions, because not every required report listed here has a formSchema
-- built yet (e.g. Site Inspection, Risk Assessment, Corrective Action) - this table can
-- describe a role's obligations ahead of the report-builder catching up, without a broken
-- foreign key. Where a report_type_code *does* match a real report_type_definitions.code
-- (INCIDENT, TOOLBOX_TALK), the frontend can still cross-reference live compliance status.

create table role_responsibilities (
    id uuid primary key,
    version bigint not null default 0,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    industry_module_id uuid not null references industry_modules (id),
    role varchar(32) not null,
    description varchar(500) not null,
    sort_order integer not null default 0
);

create index ix_role_responsibilities_module_role on role_responsibilities (industry_module_id, role);

create table role_required_reports (
    id uuid primary key,
    version bigint not null default 0,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    industry_module_id uuid not null references industry_modules (id),
    role varchar(32) not null,
    report_type_code varchar(64) not null,
    report_type_name varchar(255) not null,
    frequency_label varchar(64) not null,
    sort_order integer not null default 0
);

create index ix_role_required_reports_module_role on role_required_reports (industry_module_id, role);

-- CONSTRUCTION (00000000-0000-0000-0000-000000000001) - all 5 roles seeded so every role
-- tab has real content. Other industry modules intentionally have none yet (the Roles &
-- Responsibilities screen renders an empty state for those - a documented gap, not faked).

insert into role_responsibilities (id, industry_module_id, role, description, sort_order) values
    ('10000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'SHE_OFFICER', 'Conduct scheduled site inspections', 1),
    ('10000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000001', 'SHE_OFFICER', 'Investigate and report incidents', 2),
    ('10000000-0000-0000-0000-000000000003', '00000000-0000-0000-0000-000000000001', 'SHE_OFFICER', 'Maintain the SHE file and legal register', 3),
    ('10000000-0000-0000-0000-000000000004', '00000000-0000-0000-0000-000000000001', 'SHE_OFFICER', 'Deliver toolbox talks and safety briefings', 4),
    ('10000000-0000-0000-0000-000000000005', '00000000-0000-0000-0000-000000000001', 'SHE_OFFICER', 'Track corrective actions through to closure', 5),
    ('10000000-0000-0000-0000-000000000006', '00000000-0000-0000-0000-000000000001', 'SHE_OFFICER', 'Liaise with regulators and auditors', 6),
    ('10000000-0000-0000-0000-000000000007', '00000000-0000-0000-0000-000000000001', 'SHE_OFFICER', 'Monitor training and certification expiry', 7),

    ('10000000-0000-0000-0000-000000000011', '00000000-0000-0000-0000-000000000001', 'SITE_SUPERVISOR', 'Run daily pre-start safety briefings for the crew', 1),
    ('10000000-0000-0000-0000-000000000012', '00000000-0000-0000-0000-000000000001', 'SITE_SUPERVISOR', 'Enforce PPE and site access control', 2),
    ('10000000-0000-0000-0000-000000000013', '00000000-0000-0000-0000-000000000001', 'SITE_SUPERVISOR', 'Report incidents and near misses to the SHE Officer immediately', 3),
    ('10000000-0000-0000-0000-000000000014', '00000000-0000-0000-0000-000000000001', 'SITE_SUPERVISOR', 'Check permits (hot work, working at heights, confined space) before work starts', 4),
    ('10000000-0000-0000-0000-000000000015', '00000000-0000-0000-0000-000000000001', 'SITE_SUPERVISOR', 'Escalate unresolved hazards to the SHE Officer', 5),

    ('10000000-0000-0000-0000-000000000021', '00000000-0000-0000-0000-000000000001', 'GENERAL_WORKER', 'Follow site safety procedures and PPE requirements', 1),
    ('10000000-0000-0000-0000-000000000022', '00000000-0000-0000-0000-000000000001', 'GENERAL_WORKER', 'Report hazards, near misses and incidents to your supervisor', 2),
    ('10000000-0000-0000-0000-000000000023', '00000000-0000-0000-0000-000000000001', 'GENERAL_WORKER', 'Attend toolbox talks and safety briefings', 3),
    ('10000000-0000-0000-0000-000000000024', '00000000-0000-0000-0000-000000000001', 'GENERAL_WORKER', 'Keep certifications and training up to date', 4),

    ('10000000-0000-0000-0000-000000000031', '00000000-0000-0000-0000-000000000001', 'CONTRACTOR', 'Comply with site SHE policies and induction requirements', 1),
    ('10000000-0000-0000-0000-000000000032', '00000000-0000-0000-0000-000000000001', 'CONTRACTOR', 'Hold valid insurance, permits and certifications for the work performed', 2),
    ('10000000-0000-0000-0000-000000000033', '00000000-0000-0000-0000-000000000001', 'CONTRACTOR', 'Report incidents involving your crew or equipment', 3),
    ('10000000-0000-0000-0000-000000000034', '00000000-0000-0000-0000-000000000001', 'CONTRACTOR', 'Coordinate high-risk work with the Site Supervisor', 4),

    ('10000000-0000-0000-0000-000000000041', '00000000-0000-0000-0000-000000000001', 'MANAGEMENT', 'Review monthly SHE performance and compliance reporting', 1),
    ('10000000-0000-0000-0000-000000000042', '00000000-0000-0000-0000-000000000001', 'MANAGEMENT', 'Approve budget for corrective actions and equipment', 2),
    ('10000000-0000-0000-0000-000000000043', '00000000-0000-0000-0000-000000000001', 'MANAGEMENT', 'Ensure legal appointments and SHE Officer resourcing are current', 3),
    ('10000000-0000-0000-0000-000000000044', '00000000-0000-0000-0000-000000000001', 'MANAGEMENT', 'Chair periodic SHE committee reviews', 4);

insert into role_required_reports (id, industry_module_id, role, report_type_code, report_type_name, frequency_label, sort_order) values
    ('20000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'SHE_OFFICER', 'SITE_INSPECTION', 'Site Inspection', 'Weekly', 1),
    ('20000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000001', 'SHE_OFFICER', 'INCIDENT', 'Incident / Accident Report', 'As needed', 2),
    ('20000000-0000-0000-0000-000000000003', '00000000-0000-0000-0000-000000000001', 'SHE_OFFICER', 'RISK_ASSESSMENT', 'Risk Assessment', 'Monthly / per task', 3),
    ('20000000-0000-0000-0000-000000000004', '00000000-0000-0000-0000-000000000001', 'SHE_OFFICER', 'TOOLBOX_TALK', 'Toolbox Talk Record', 'Weekly', 4),
    ('20000000-0000-0000-0000-000000000005', '00000000-0000-0000-0000-000000000001', 'SHE_OFFICER', 'CORRECTIVE_ACTION', 'Corrective Action Log', 'Ongoing', 5),

    ('20000000-0000-0000-0000-000000000011', '00000000-0000-0000-0000-000000000001', 'SITE_SUPERVISOR', 'TOOLBOX_TALK', 'Toolbox Talk Record', 'Weekly', 1),
    ('20000000-0000-0000-0000-000000000012', '00000000-0000-0000-0000-000000000001', 'SITE_SUPERVISOR', 'INCIDENT', 'Incident / Accident Report', 'As needed', 2),
    ('20000000-0000-0000-0000-000000000013', '00000000-0000-0000-0000-000000000001', 'SITE_SUPERVISOR', 'SITE_INSPECTION', 'Site Inspection', 'Weekly', 3),

    ('20000000-0000-0000-0000-000000000021', '00000000-0000-0000-0000-000000000001', 'GENERAL_WORKER', 'INCIDENT', 'Incident / Accident Report', 'As needed', 1),

    ('20000000-0000-0000-0000-000000000031', '00000000-0000-0000-0000-000000000001', 'CONTRACTOR', 'INCIDENT', 'Incident / Accident Report', 'As needed', 1),
    ('20000000-0000-0000-0000-000000000032', '00000000-0000-0000-0000-000000000001', 'CONTRACTOR', 'SITE_INSPECTION', 'Site Inspection', 'Weekly', 2),

    ('20000000-0000-0000-0000-000000000041', '00000000-0000-0000-0000-000000000001', 'MANAGEMENT', 'CORRECTIVE_ACTION', 'Corrective Action Log', 'Ongoing', 1),
    ('20000000-0000-0000-0000-000000000042', '00000000-0000-0000-0000-000000000001', 'MANAGEMENT', 'MANAGEMENT_REPORT', 'Monthly SHE Performance Report', 'Monthly', 2);
