#!/usr/bin/env bash
# Starts local Postgres (docker compose) and the backend app with the
# `local` Spring profile (demo data included). Pair with stop.sh.
set -euo pipefail

cd "$(dirname "${BASH_SOURCE[0]}")"

RUN_DIR=".run"
LOG_FILE="$RUN_DIR/app.log"
PID_FILE="$RUN_DIR/app.pid"
APP_URL="http://localhost:8080/actuator/health"

mkdir -p "$RUN_DIR"

if [[ -f "$PID_FILE" ]] && kill -0 "$(cat "$PID_FILE")" 2>/dev/null; then
    echo "App already running (pid $(cat "$PID_FILE")). Run ./stop.sh first."
    exit 1
fi

echo "Starting Postgres..."
docker compose up -d

echo "Waiting for Postgres to be healthy..."
container_id="$(docker compose ps -q postgres)"
healthy=false
for _ in $(seq 1 30); do
    if [[ "$(docker inspect --format '{{.State.Health.Status}}' "$container_id" 2>/dev/null)" == "healthy" ]]; then
        healthy=true
        break
    fi
    sleep 1
done
if [[ "$healthy" != true ]]; then
    echo "Postgres did not become healthy in time. Check 'docker compose logs postgres'."
    exit 1
fi

echo "Starting the app (local profile)..."
nohup ./gradlew bootRun --args='--spring.profiles.active=local' > "$LOG_FILE" 2>&1 &
echo $! > "$PID_FILE"

echo "Waiting for the app to come up..."
for _ in $(seq 1 60); do
    if curl -s -o /dev/null -w '%{http_code}' "$APP_URL" 2>/dev/null | grep -q 200; then
        echo "App is up: $APP_URL"
        echo "Logs: tail -f $LOG_FILE"
        exit 0
    fi
    sleep 2
done

echo "App did not become healthy in time. Check $LOG_FILE."
exit 1
