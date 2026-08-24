-- Spring Modulith's durable event log (spring-modulith-starter-jpa): every event
-- publication is persisted here before listeners run, so a crash between "report
-- submitted" and "notification sent" doesn't silently drop the notification - on
-- restart Modulith resubmits anything left incomplete. Schema mirrors exactly what
-- Hibernate generates for org.springframework.modulith.events.jpa.JpaEventPublication.
create table event_publication (
    id uuid not null,
    completion_attempts integer not null,
    completion_date timestamptz,
    last_resubmission_date timestamptz,
    publication_date timestamptz not null,
    event_type varchar(255) not null,
    listener_id varchar(255) not null,
    serialized_event varchar(255) not null,
    status varchar(255)
        constraint event_publication_status_check
        check (status in ('PUBLISHED', 'PROCESSING', 'COMPLETED', 'FAILED', 'RESUBMITTED')),
    primary key (id)
);
