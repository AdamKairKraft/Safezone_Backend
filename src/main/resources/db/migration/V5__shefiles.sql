create table she_files (
    id uuid primary key,
    version bigint not null default 0,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    organization_id uuid not null,
    site_id uuid,
    category varchar(64) not null,
    title varchar(255) not null,
    owner_user_id uuid,
    storage_key varchar(512) not null,
    expiry_date date
);

create index ix_she_files_organization_id on she_files (organization_id);
