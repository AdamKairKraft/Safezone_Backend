#!/usr/bin/env bash
# Stops the backend app and the local Postgres container started by start.sh.
set -uo pipefail

cd "$(dirname "${BASH_SOURCE[0]}")"

RUN_DIR=".run"
PID_FILE="$RUN_DIR/app.pid"

echo "Stopping the app..."
if [[ -f "$PID_FILE" ]]; then
    kill "$(cat "$PID_FILE")" 2>/dev/null || true
    rm -f "$PID_FILE"
fi
# bootRun's PID above is the Gradle wrapper, not the Spring Boot JVM itself
# (Gradle runs it through its daemon), so make sure that's gone too.
pkill -f "com.safezone.SafezoneBackendApplication" 2>/dev/null || true

echo "Stopping Postgres..."
docker compose down

echo "Done. Data is kept in the docker volume - run 'docker compose down -v' to wipe it."
