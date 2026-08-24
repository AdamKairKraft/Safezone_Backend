-- Seed the industry module catalog so the app is demonstrably multi-domain out of
-- the box: adding a new safety domain is a data change here, not a code change.
insert into industry_modules (id, code, name, description, created_at, updated_at) values
    ('00000000-0000-0000-0000-000000000001', 'CONSTRUCTION', 'Construction', 'Construction site health and safety', now(), now()),
    ('00000000-0000-0000-0000-000000000002', 'FOOD_SAFETY', 'Food Safety', 'Food handling and HACCP compliance', now(), now()),
    ('00000000-0000-0000-0000-000000000003', 'AVIATION', 'Aviation Safety', 'Ramp and flight-line safety', now(), now()),
    ('00000000-0000-0000-0000-000000000004', 'FIRE_SAFETY', 'Fire Safety', 'Fire prevention and drill compliance', now(), now()),
    ('00000000-0000-0000-0000-000000000005', 'FIRST_AID', 'First Aid Safety', 'First aid readiness and incident response', now(), now());

insert into report_type_definitions (id, industry_module_id, code, name, form_schema, created_at, updated_at) values
    ('00000000-0000-0000-0000-000000000101', '00000000-0000-0000-0000-000000000001', 'INCIDENT',
     'Incident / Accident',
     '{"fields":[{"name":"dateTime","label":"Date & Time","type":"datetime","required":true},{"name":"location","label":"Site / Location","type":"text","required":true},{"name":"severity","label":"Severity","type":"select","options":["LOW","MEDIUM","HIGH"],"required":true},{"name":"description","label":"Description of Event","type":"textarea","required":true}]}'::jsonb,
     now(), now()),
    ('00000000-0000-0000-0000-000000000102', '00000000-0000-0000-0000-000000000001', 'TOOLBOX_TALK',
     'Toolbox Talk',
     '{"fields":[{"name":"dateTime","label":"Date & Time","type":"datetime","required":true},{"name":"topic","label":"Topic","type":"text","required":true},{"name":"attendees","label":"Attendees","type":"text","required":true}]}'::jsonb,
     now(), now()),
    ('00000000-0000-0000-0000-000000000201', '00000000-0000-0000-0000-000000000002', 'HACCP_CHECK',
     'HACCP Check',
     '{"fields":[{"name":"dateTime","label":"Date & Time","type":"datetime","required":true},{"name":"controlPoint","label":"Critical Control Point","type":"text","required":true},{"name":"temperature","label":"Temperature (C)","type":"number","required":false}]}'::jsonb,
     now(), now()),
    ('00000000-0000-0000-0000-000000000301', '00000000-0000-0000-0000-000000000003', 'PREFLIGHT_INSPECTION',
     'Pre-flight Inspection',
     '{"fields":[{"name":"dateTime","label":"Date & Time","type":"datetime","required":true},{"name":"aircraftId","label":"Aircraft ID","type":"text","required":true},{"name":"findings","label":"Findings","type":"textarea","required":false}]}'::jsonb,
     now(), now()),
    ('00000000-0000-0000-0000-000000000401', '00000000-0000-0000-0000-000000000004', 'FIRE_DRILL',
     'Fire Drill',
     '{"fields":[{"name":"dateTime","label":"Date & Time","type":"datetime","required":true},{"name":"evacuationTimeSeconds","label":"Evacuation Time (s)","type":"number","required":true}]}'::jsonb,
     now(), now()),
    ('00000000-0000-0000-0000-000000000501', '00000000-0000-0000-0000-000000000005', 'FIRST_AID_INCIDENT',
     'First Aid Incident',
     '{"fields":[{"name":"dateTime","label":"Date & Time","type":"datetime","required":true},{"name":"treatmentGiven","label":"Treatment Given","type":"textarea","required":true}]}'::jsonb,
     now(), now());

insert into compliance_category_definitions (id, industry_module_id, code, name, created_at, updated_at) values
    ('00000000-0000-0000-0000-000000000111', '00000000-0000-0000-0000-000000000001', 'TOOLBOX_TALKS', 'Toolbox Talks', now(), now()),
    ('00000000-0000-0000-0000-000000000211', '00000000-0000-0000-0000-000000000002', 'HACCP_CHECKS', 'HACCP Checks', now(), now()),
    ('00000000-0000-0000-0000-000000000311', '00000000-0000-0000-0000-000000000003', 'PREFLIGHT_INSPECTIONS', 'Pre-flight Inspections', now(), now()),
    ('00000000-0000-0000-0000-000000000411', '00000000-0000-0000-0000-000000000004', 'FIRE_DRILLS', 'Fire Drills', now(), now()),
    ('00000000-0000-0000-0000-000000000511', '00000000-0000-0000-0000-000000000005', 'FIRST_AID_INCIDENTS', 'First Aid Incidents', now(), now());
