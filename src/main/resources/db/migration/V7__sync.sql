create table sync_conflicts (
    id uuid primary key,
    version bigint not null default 0,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    entity_type varchar(64) not null,
    entity_id uuid not null,
    base_version bigint not null,
    losing_payload jsonb not null,
    winning_payload jsonb not null,
    submitted_by uuid,
    submitted_at timestamptz not null default now(),
    status varchar(32) not null default 'PENDING',
    resolution varchar(32),
    resolved_by uuid,
    resolved_at timestamptz
);

create index ix_sync_conflicts_status on sync_conflicts (status);
