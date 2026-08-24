create table idempotency_records (
    id uuid primary key,
    version bigint not null default 0,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    idempotency_key varchar(255) not null,
    request_hash varchar(64) not null,
    response_status integer not null,
    response_body text
);

create unique index ux_idempotency_records_key on idempotency_records (idempotency_key);
