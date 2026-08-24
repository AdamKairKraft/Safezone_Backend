-- Demo/local-only data. Only applied when spring.flyway.locations includes
-- classpath:db/seed (see application-local.properties) - never in staging or
-- production. Repeatable migration (R__) so editing this file re-seeds on the
-- next local startup instead of requiring a new version number.
--
-- Two orgs across the two most-built-out industry modules (CONSTRUCTION,
-- FOOD_SAFETY) so every table has rows to explore, including edge cases:
-- an overdue compliance requirement, an expired SHE file, and a draft report.

-- Organizations
insert into organizations (id, version, created_at, updated_at, name) values
    ('a0000000-0000-0000-0000-000000000001', 0, now(), now(), 'Acme Construction Group'),
    ('a0000000-0000-0000-0000-000000000002', 0, now(), now(), 'Skyline Foods Co')
on conflict (id) do nothing;

-- Sites
insert into sites (id, version, created_at, updated_at, organization_id, name) values
    ('b0000000-0000-0000-0000-000000000001', 0, now(), now(), 'a0000000-0000-0000-0000-000000000001', 'Downtown Tower Project'),
    ('b0000000-0000-0000-0000-000000000002', 0, now(), now(), 'a0000000-0000-0000-0000-000000000001', 'Harbor Bridge Project'),
    ('b0000000-0000-0000-0000-000000000003', 0, now(), now(), 'a0000000-0000-0000-0000-000000000002', 'Central Kitchen'),
    ('b0000000-0000-0000-0000-000000000004', 0, now(), now(), 'a0000000-0000-0000-0000-000000000002', 'Cold Storage Warehouse')
on conflict (id) do nothing;

-- App users. password_hash is a BCrypt hash of the shared demo password "SafeZone123!"
-- (see safezone-backend/README.md "Demo credentials") - local/demo data only, never used
-- outside this seed.
insert into app_users (id, version, created_at, updated_at, organization_id, email, full_name, password_hash) values
    ('c0000000-0000-0000-0000-000000000001', 0, now(), now(), 'a0000000-0000-0000-0000-000000000001', 'jane.smith@acme-construction.test', 'Jane Smith', '$2a$10$.cqRrB/dJEnQuLKwitHuHuuixvwUQZdRQU2gXtUiaQY3fFWBYerve'),
    ('c0000000-0000-0000-0000-000000000002', 0, now(), now(), 'a0000000-0000-0000-0000-000000000001', 'tom.reid@acme-construction.test', 'Tom Reid', '$2a$10$.cqRrB/dJEnQuLKwitHuHuuixvwUQZdRQU2gXtUiaQY3fFWBYerve'),
    ('c0000000-0000-0000-0000-000000000003', 0, now(), now(), 'a0000000-0000-0000-0000-000000000001', 'lindiwe.mokoena@acme-construction.test', 'Lindiwe Mokoena', '$2a$10$.cqRrB/dJEnQuLKwitHuHuuixvwUQZdRQU2gXtUiaQY3fFWBYerve'),
    ('c0000000-0000-0000-0000-000000000004', 0, now(), now(), 'a0000000-0000-0000-0000-000000000002', 'priya.naidoo@skylinefoods.test', 'Priya Naidoo', '$2a$10$.cqRrB/dJEnQuLKwitHuHuuixvwUQZdRQU2gXtUiaQY3fFWBYerve'),
    ('c0000000-0000-0000-0000-000000000005', 0, now(), now(), 'a0000000-0000-0000-0000-000000000002', 'carlos.mendes@skylinefoods.test', 'Carlos Mendes', '$2a$10$.cqRrB/dJEnQuLKwitHuHuuixvwUQZdRQU2gXtUiaQY3fFWBYerve'),
    ('c0000000-0000-0000-0000-000000000006', 0, now(), now(), 'a0000000-0000-0000-0000-000000000002', 'anna.petrova@skylinefoods.test', 'Anna Petrova', '$2a$10$.cqRrB/dJEnQuLKwitHuHuuixvwUQZdRQU2gXtUiaQY3fFWBYerve')
on conflict (id) do nothing;

-- Since this is a repeatable migration, an already-seeded environment (from before
-- password_hash existed) needs the column backfilled explicitly - "on conflict do nothing"
-- above wouldn't touch existing rows.
update app_users set password_hash = '$2a$10$.cqRrB/dJEnQuLKwitHuHuuixvwUQZdRQU2gXtUiaQY3fFWBYerve' where password_hash = '';

insert into app_user_roles (app_user_id, role) values
    ('c0000000-0000-0000-0000-000000000001', 'SHE_OFFICER'),
    ('c0000000-0000-0000-0000-000000000001', 'MANAGEMENT'),
    ('c0000000-0000-0000-0000-000000000002', 'SITE_SUPERVISOR'),
    ('c0000000-0000-0000-0000-000000000003', 'GENERAL_WORKER'),
    ('c0000000-0000-0000-0000-000000000004', 'SHE_OFFICER'),
    ('c0000000-0000-0000-0000-000000000005', 'SITE_SUPERVISOR'),
    ('c0000000-0000-0000-0000-000000000006', 'CONTRACTOR')
on conflict (app_user_id, role) do nothing;

-- Reports
insert into reports (id, version, created_at, updated_at, organization_id, site_id, industry_module_code, report_type_code, status, data, submitted_by, client_created_at, server_received_at) values
    ('d0000000-0000-0000-0000-000000000001', 0, now() - interval '3 days', now() - interval '3 days',
     'a0000000-0000-0000-0000-000000000001', 'b0000000-0000-0000-0000-000000000001', 'CONSTRUCTION', 'INCIDENT', 'SUBMITTED',
     '{"dateTime":"2026-08-21T09:15:00Z","location":"Downtown Tower - Level 12","severity":"MEDIUM","description":"Worker slipped on wet scaffolding boards after overnight rain; no lost time, first aid administered on site."}'::jsonb,
     'c0000000-0000-0000-0000-000000000002', now() - interval '3 days', now() - interval '3 days'),
    ('d0000000-0000-0000-0000-000000000002', 0, now() - interval '7 days', now() - interval '7 days',
     'a0000000-0000-0000-0000-000000000001', 'b0000000-0000-0000-0000-000000000002', 'CONSTRUCTION', 'TOOLBOX_TALK', 'CLOSED',
     '{"dateTime":"2026-08-17T07:30:00Z","topic":"Working at height - harness inspection","attendees":"14 crew members"}'::jsonb,
     'c0000000-0000-0000-0000-000000000002', now() - interval '7 days', now() - interval '7 days'),
    ('d0000000-0000-0000-0000-000000000003', 0, now() - interval '1 hours', now() - interval '1 hours',
     'a0000000-0000-0000-0000-000000000001', 'b0000000-0000-0000-0000-000000000001', 'CONSTRUCTION', 'TOOLBOX_TALK', 'DRAFT',
     '{"dateTime":"2026-08-24T06:00:00Z","topic":"","attendees":""}'::jsonb,
     null, now() - interval '1 hours', now() - interval '1 hours'),
    ('d0000000-0000-0000-0000-000000000004', 0, now() - interval '2 days', now() - interval '2 days',
     'a0000000-0000-0000-0000-000000000002', 'b0000000-0000-0000-0000-000000000003', 'FOOD_SAFETY', 'HACCP_CHECK', 'SUBMITTED',
     '{"dateTime":"2026-08-22T11:00:00Z","controlPoint":"Walk-in chiller #2","temperature":4.1}'::jsonb,
     'c0000000-0000-0000-0000-000000000004', now() - interval '2 days', now() - interval '2 days'),
    ('d0000000-0000-0000-0000-000000000005', 0, now() - interval '5 days', now() - interval '1 days',
     'a0000000-0000-0000-0000-000000000002', 'b0000000-0000-0000-0000-000000000004', 'FOOD_SAFETY', 'HACCP_CHECK', 'UNDER_REVIEW',
     '{"dateTime":"2026-08-19T15:45:00Z","controlPoint":"Freezer bay 1 receiving dock","temperature":-16.5}'::jsonb,
     'c0000000-0000-0000-0000-000000000005', now() - interval '5 days', now() - interval '5 days')
on conflict (id) do nothing;

-- SHE files (expiry_date drives status(): past = EXPIRED, within 30 days = EXPIRING_SOON, else/null = VALID)
insert into she_files (id, version, created_at, updated_at, organization_id, site_id, category, title, owner_user_id, storage_key, expiry_date) values
    ('e0000000-0000-0000-0000-000000000001', 0, now(), now(),
     'a0000000-0000-0000-0000-000000000001', 'b0000000-0000-0000-0000-000000000001', 'LEGAL_APPOINTMENT',
     'SHE Officer Appointment - Jane Smith', 'c0000000-0000-0000-0000-000000000001',
     'demo/acme/legal-appointment-jane-smith.pdf', date '2027-06-01'),
    ('e0000000-0000-0000-0000-000000000002', 0, now(), now(),
     'a0000000-0000-0000-0000-000000000001', 'b0000000-0000-0000-0000-000000000002', 'TRAINING_CERTIFICATE',
     'Fall Protection Training - Tom Reid', 'c0000000-0000-0000-0000-000000000002',
     'demo/acme/fall-protection-tom-reid.pdf', current_date + 10),
    ('e0000000-0000-0000-0000-000000000003', 0, now(), now(),
     'a0000000-0000-0000-0000-000000000001', 'b0000000-0000-0000-0000-000000000002', 'EQUIPMENT_CERTIFICATE',
     'Tower Crane Certificate - TC-04', null,
     'demo/acme/tower-crane-tc-04.pdf', current_date - 15),
    ('e0000000-0000-0000-0000-000000000004', 0, now(), now(),
     'a0000000-0000-0000-0000-000000000002', null, 'POLICY_PROCEDURE',
     'HACCP Plan v3', 'c0000000-0000-0000-0000-000000000004',
     'demo/skyline/haccp-plan-v3.pdf', null),
    ('e0000000-0000-0000-0000-000000000005', 0, now(), now(),
     'a0000000-0000-0000-0000-000000000002', 'b0000000-0000-0000-0000-000000000003', 'PERMIT_LICENSE',
     'Food Handling Permit - Central Kitchen', 'c0000000-0000-0000-0000-000000000004',
     'demo/skyline/food-handling-permit-central.pdf', current_date + 5)
on conflict (id) do nothing;

-- Compliance requirements
insert into compliance_requirements (id, version, created_at, updated_at, organization_id, site_id, industry_module_code, category_code, report_type_code, frequency, last_completed_at) values
    ('f0000000-0000-0000-0000-000000000001', 0, now(), now(),
     'a0000000-0000-0000-0000-000000000001', 'b0000000-0000-0000-0000-000000000001',
     'CONSTRUCTION', 'TOOLBOX_TALKS', 'TOOLBOX_TALK', 'WEEKLY', now() - interval '2 days'),
    ('f0000000-0000-0000-0000-000000000002', 0, now(), now(),
     'a0000000-0000-0000-0000-000000000001', 'b0000000-0000-0000-0000-000000000002',
     'CONSTRUCTION', 'TOOLBOX_TALKS', 'TOOLBOX_TALK', 'WEEKLY', null),
    ('f0000000-0000-0000-0000-000000000003', 0, now(), now(),
     'a0000000-0000-0000-0000-000000000002', 'b0000000-0000-0000-0000-000000000003',
     'FOOD_SAFETY', 'HACCP_CHECKS', 'HACCP_CHECK', 'MONTHLY', now() - interval '3 days'),
    ('f0000000-0000-0000-0000-000000000004', 0, now(), now(),
     'a0000000-0000-0000-0000-000000000002', 'b0000000-0000-0000-0000-000000000004',
     'FOOD_SAFETY', 'HACCP_CHECKS', 'HACCP_CHECK', 'MONTHLY', now() - interval '40 days')
on conflict (id) do nothing;
