create table organizations (
    id uuid primary key,
    version bigint not null default 0,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    name varchar(255) not null
);

create table sites (
    id uuid primary key,
    version bigint not null default 0,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    organization_id uuid not null references organizations (id),
    name varchar(255) not null
);

create index ix_sites_organization_id on sites (organization_id);

create table app_users (
    id uuid primary key,
    version bigint not null default 0,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    organization_id uuid not null references organizations (id),
    email varchar(255) not null,
    full_name varchar(255) not null
);

create unique index ux_app_users_email on app_users (email);
create index ix_app_users_organization_id on app_users (organization_id);

create table app_user_roles (
    app_user_id uuid not null references app_users (id),
    role varchar(64) not null,
    primary key (app_user_id, role)
);
