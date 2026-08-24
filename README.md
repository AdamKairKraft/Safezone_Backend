# SafeZone Backend

SafeZone's backend: a Spring Modulith modular monolith for tracking site safety and
compliance across industries (construction, food safety, aviation, fire safety, first
aid). Each business area lives in its own module under `src/main/java/com/safezone/`:

| Module | Responsibility |
|---|---|
| `identity` | Organizations, sites, users and roles |
| `industrymodule` | Catalog of industry modules, report types, and compliance categories |
| `reporting` | Submitted safety reports (incidents, toolbox talks, checks, etc.) |
| `compliance` | Tracking whether a site is up to date on its required reports |
| `shefiles` | SHE (Safety, Health & Environment) documents — permits, certificates, policies |
| `sync` | Offline-first sync and conflict resolution for clients pushing local changes |
| `notification` | Reacts to domain events (e.g. a submitted report) and notifies |
| `shared` | Cross-module base types, idempotency, and web error handling |

## Prerequisites

- **JDK 21** (the Gradle wrapper will use whatever `java` toolchain it finds/downloads for this version)
- **Docker** — used to run Postgres locally and by the test suite (Testcontainers)
- **Git**

## Branching

- `dev` — where all work lands. Every change goes through a pull request into `dev`
  requiring 2 approvals and a passing CI build; no one can push to it directly.
- `staging` / `production` — updated automatically. Merging a PR into `dev` triggers a
  workflow that opens and auto-merges a promotion PR into each, once CI passes there too.
  Nobody pushes to these branches by hand.

Day to day: branch off `dev`, open a PR back into `dev`.

## Running locally

> **Once this is deployed to the cloud**, dev/staging/production will each have their
> own cloud-hosted Postgres database — the `docker compose` Postgres setup below is
> local-only and won't be used at that point. Docker's role shifts to containerizing
> the *app itself* for deployment, rather than running its database.

### Quick start

```bash
./start.sh   # starts Postgres + the app (local profile, demo data included)
./stop.sh    # stops the app and Postgres
```

`start.sh` waits for Postgres to be healthy, then runs the app in the background,
writing logs to `.run/app.log` (`tail -f .run/app.log` to follow them) and its pid to
`.run/app.pid`. `stop.sh` stops both. Postgres data persists in a docker volume across
`stop.sh`/`start.sh` cycles — see the manual steps below to wipe it.

The rest of this section is what those scripts do, spelled out, if you want to run
steps individually or understand what's going on.

### 1. Start the database

```bash
docker compose up -d
```

This starts a Postgres 16 container (`docker-compose.yml`) on `localhost:5432` with a
persistent volume, database `safezone`, user/password `safezone`/`safezone`.

> Port 5432 already in use on your machine? Either stop whatever's using it, or change
> the `ports` mapping in `docker-compose.yml` and the matching
> `spring.datasource.url` in `src/main/resources/application-local.properties`.

### 2. Run the app

```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```

The `local` profile (`application-local.properties`) points the app at the Postgres
container above and adds `db/seed` to the Flyway migration path, so demo data loads
automatically. On startup, Flyway runs every schema migration in
`src/main/resources/db/migration`, then the demo data seed described below.

The app listens on `http://localhost:8080`. Check it's up:

```bash
curl http://localhost:8080/actuator/health
```

Other useful actuator endpoints: `/actuator/info`, `/actuator/modulith` (module structure).

### 3. Stopping / resetting

```bash
docker compose down       # stop Postgres, keep data
docker compose down -v    # stop Postgres and wipe the local database entirely
```

Wipe and restart if you want a totally clean re-seed.

### Alternative: zero-setup dev run

`./gradlew bootTestRun` starts the app using Testcontainers to spin up a throwaway
Postgres for you automatically — no `docker compose` step needed. It doesn't use the
`local` profile, so it **won't** load demo data, and the database is destroyed when you
stop the app. Good for a quick check; use the `docker compose` path above for anything
you want to keep or seed.

## Demo/seed data

`src/main/resources/db/seed/R__demo_data.sql` is a Flyway repeatable migration that
only runs when the `local` profile is active (never in staging/production/CI — see
`spring.flyway.locations` in `application-local.properties`). It seeds:

- **2 organizations**: Acme Construction Group, Skyline Foods Co
- **4 sites** across them (2 each)
- **6 users** spanning every role (`SHE_OFFICER`, `SITE_SUPERVISOR`, `GENERAL_WORKER`,
  `CONTRACTOR`, `MANAGEMENT`)
- **5 reports** covering different types and statuses, including a `DRAFT`
- **5 SHE files**, deliberately spanning valid, expiring-soon, and expired
- **4 compliance requirements**, including one that's overdue

If you edit this file, Flyway will re-apply it on next startup (repeatable migrations
re-run when their checksum changes). Inserts use `on conflict do nothing`, so re-running
against a database that already has this data is safe — to fully re-seed after an edit,
run `docker compose down -v` first.

## Running tests

```bash
./gradlew test
```

Tests use Testcontainers to spin up and tear down their own Postgres instance
automatically — Docker must be running, but you don't need `docker compose up` for
tests.
