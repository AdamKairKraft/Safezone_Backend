-- Spring Modulith's JpaEventPublication entity leaves serializedEvent unannotated for
-- length, so Hibernate's own default (varchar(255), matched by V9) is what schema
-- validation expects - but real event JSON payloads routinely exceed 255 characters
-- (e.g. ReportSubmitted carries the full report id/org/site UUIDs plus metadata),
-- so every submit failed with "value too long for type character varying(255)".
alter table event_publication alter column serialized_event type text;
