-- Password hash for real login (was previously unauthenticated - X-User-Id headers were
-- trusted at face value). Backfilled immediately below since app_users already has rows
-- in every environment's demo/seed data path by the time this runs there.
alter table app_users add column password_hash varchar(255) not null default '';
alter table app_users alter column password_hash drop default;

-- Refresh tokens are stored as a hash (same pattern IdempotencyGuard already uses for
-- request hashing), never the raw value, so a DB read alone can't be used to impersonate
-- a session. Rotated on every use (old row revoked, new row inserted) in AuthService.
create table refresh_tokens (
    id uuid primary key,
    app_user_id uuid not null references app_users (id),
    token_hash varchar(255) not null,
    expires_at timestamptz not null,
    revoked_at timestamptz,
    created_at timestamptz not null default now()
);

create index ix_refresh_tokens_app_user_id on refresh_tokens (app_user_id);
create unique index ux_refresh_tokens_token_hash on refresh_tokens (token_hash);
