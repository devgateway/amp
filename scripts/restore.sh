#!/usr/bin/env bash
# =============================================================================
# AMP Database Restore
# =============================================================================
# Usage:
#   ./restore.sh <backup-file> [--docker|--baremetal]
#
#   <backup-file>   Path to a .backup or .backup.7z file.
#   --docker        Stop/start the AMP Docker container around the restore.
#   --baremetal     Stop/start Tomcat around the restore.
#                   If neither flag is given the script restores the database
#                   without touching the application layer.
#
# Configuration — set as environment variables:
#
#   AMP_DB_HOST         (default: localhost)
#   AMP_DB_PORT         (default: 5432)
#   AMP_DB_NAME         (default: amp)
#   AMP_DB_USER         (default: amp)
#   AMP_DB_SUPERUSER    (default: postgres)  Used to drop/recreate the database.
#   PGPASSWORD          Password for AMP_DB_USER.
#   PGPASSWORD_SUPER    Password for AMP_DB_SUPERUSER (if different).
#   AMP_CONTAINER       Docker container name (default: amp)
#   AMP_TOMCAT_HOME     Tomcat base directory (default: /opt/tomcat)
#
# !! CAUTION: This script drops and recreates AMP_DB_NAME. !!
# !! Run against a TEST or RECOVERY environment, not production, !!
# !! unless you are performing an authorized production restore.  !!
#
# Exit codes:
#   0  success
#   1  bad arguments or missing dependencies
#   2  decompress failed
#   3  database drop/create failed
#   4  pg_restore failed
# =============================================================================

set -euo pipefail

# ---------------------------------------------------------------------------
# Arguments
# ---------------------------------------------------------------------------
if [[ $# -lt 1 ]]; then
  echo "Usage: $0 <backup-file> [--docker|--baremetal]" >&2
  exit 1
fi

BACKUP_ARG="$1"
DEPLOY_MODE=""
for arg in "${@:2}"; do
  case "$arg" in
    --docker)     DEPLOY_MODE="docker" ;;
    --baremetal)  DEPLOY_MODE="baremetal" ;;
  esac
done

# ---------------------------------------------------------------------------
# Configuration
# ---------------------------------------------------------------------------
AMP_DB_HOST="${AMP_DB_HOST:-localhost}"
AMP_DB_PORT="${AMP_DB_PORT:-5432}"
AMP_DB_NAME="${AMP_DB_NAME:-amp}"
AMP_DB_USER="${AMP_DB_USER:-amp}"
AMP_DB_SUPERUSER="${AMP_DB_SUPERUSER:-postgres}"
AMP_CONTAINER="${AMP_CONTAINER:-amp}"
AMP_TOMCAT_HOME="${AMP_TOMCAT_HOME:-/opt/tomcat}"

PGPASSWORD_SUPER="${PGPASSWORD_SUPER:-${PGPASSWORD:-}}"

# ---------------------------------------------------------------------------
# Dependency checks
# ---------------------------------------------------------------------------
for cmd in pg_restore psql; do
  if ! command -v "$cmd" >/dev/null 2>&1; then
    echo "[ERROR] ${cmd} not found. Install postgresql-client." >&2
    exit 1
  fi
done

if [[ -z "${PGPASSWORD:-}" ]]; then
  echo "[ERROR] PGPASSWORD is not set." >&2
  exit 1
fi

# ---------------------------------------------------------------------------
# Locate and decompress backup file
# ---------------------------------------------------------------------------
BACKUP_FILE="$BACKUP_ARG"

if [[ "$BACKUP_FILE" == *.7z ]]; then
  if ! command -v 7za >/dev/null 2>&1; then
    echo "[ERROR] Backup is compressed but 7za is not installed." >&2
    echo "        Install p7zip-full: apt install p7zip-full" >&2
    exit 1
  fi
  UNCOMPRESSED="${BACKUP_FILE%.7z}"
  echo "[INFO] Decompressing ${BACKUP_FILE} ..."
  7za e -y "$BACKUP_FILE" -o"$(dirname "$BACKUP_FILE")" >/dev/null
  if [[ ! -f "$UNCOMPRESSED" ]]; then
    echo "[ERROR] Decompression did not produce expected file: ${UNCOMPRESSED}" >&2
    exit 2
  fi
  BACKUP_FILE="$UNCOMPRESSED"
  CLEANUP_UNCOMPRESSED=1
else
  CLEANUP_UNCOMPRESSED=0
fi

if [[ ! -f "$BACKUP_FILE" ]]; then
  echo "[ERROR] Backup file not found: ${BACKUP_FILE}" >&2
  exit 1
fi

echo "[INFO] Backup file: ${BACKUP_FILE}"

# ---------------------------------------------------------------------------
# Stop application
# ---------------------------------------------------------------------------
stop_app() {
  case "$DEPLOY_MODE" in
    docker)
      echo "[INFO] Stopping Docker container: ${AMP_CONTAINER}"
      docker stop "$AMP_CONTAINER" || true
      ;;
    baremetal)
      echo "[INFO] Stopping Tomcat: ${AMP_TOMCAT_HOME}/bin/shutdown.sh"
      "${AMP_TOMCAT_HOME}/bin/shutdown.sh" || true
      sleep 10
      ;;
    *)
      echo "[INFO] No deploy mode specified; skipping application stop."
      ;;
  esac
}

start_app() {
  case "$DEPLOY_MODE" in
    docker)
      echo "[INFO] Starting Docker container: ${AMP_CONTAINER}"
      docker start "$AMP_CONTAINER"
      ;;
    baremetal)
      echo "[INFO] Starting Tomcat: ${AMP_TOMCAT_HOME}/bin/startup.sh"
      "${AMP_TOMCAT_HOME}/bin/startup.sh"
      ;;
    *)
      echo "[INFO] No deploy mode specified; skipping application start."
      ;;
  esac
}

stop_app

# ---------------------------------------------------------------------------
# Drop and recreate the target database
# ---------------------------------------------------------------------------
echo "[INFO] Dropping database: ${AMP_DB_NAME}"
PGPASSWORD="$PGPASSWORD_SUPER" psql \
  --host="$AMP_DB_HOST" \
  --port="$AMP_DB_PORT" \
  --username="$AMP_DB_SUPERUSER" \
  --no-password \
  --command="DROP DATABASE IF EXISTS ${AMP_DB_NAME};"

if [[ $? -ne 0 ]]; then
  echo "[ERROR] DROP DATABASE failed." >&2
  exit 3
fi

echo "[INFO] Creating database: ${AMP_DB_NAME}"
PGPASSWORD="$PGPASSWORD_SUPER" psql \
  --host="$AMP_DB_HOST" \
  --port="$AMP_DB_PORT" \
  --username="$AMP_DB_SUPERUSER" \
  --no-password \
  --command="CREATE DATABASE ${AMP_DB_NAME} OWNER ${AMP_DB_USER} ENCODING 'UTF8';"

if [[ $? -ne 0 ]]; then
  echo "[ERROR] CREATE DATABASE failed." >&2
  exit 3
fi

# ---------------------------------------------------------------------------
# Restore
# ---------------------------------------------------------------------------
echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') Restoring from ${BACKUP_FILE} ..."
pg_restore \
  --host="$AMP_DB_HOST" \
  --port="$AMP_DB_PORT" \
  --username="$AMP_DB_USER" \
  --no-password \
  --dbname="$AMP_DB_NAME" \
  --verbose \
  "$BACKUP_FILE" 2>&1 | grep -E '^\[|error|warning|pg_restore' | head -80

RESTORE_EXIT=${PIPESTATUS[0]}
if [[ $RESTORE_EXIT -ne 0 ]]; then
  echo "[ERROR] pg_restore exited with code ${RESTORE_EXIT}. Check output above." >&2
  echo "[ERROR] The database may be partially restored. Investigate before starting the application." >&2
  exit 4
fi

echo "[INFO] Restore complete."

# ---------------------------------------------------------------------------
# Basic connectivity smoke test
# ---------------------------------------------------------------------------
echo "[INFO] Running connectivity check..."
PGPASSWORD="$PGPASSWORD" psql \
  --host="$AMP_DB_HOST" \
  --port="$AMP_DB_PORT" \
  --username="$AMP_DB_USER" \
  --dbname="$AMP_DB_NAME" \
  --no-password \
  --tuples-only \
  --command="SELECT COUNT(*) FROM amp_team;" 2>/dev/null \
  && echo "[INFO] Smoke test passed: amp_team table is readable." \
  || echo "[WARN] Smoke test query failed. Verify the database manually before starting the application."

# ---------------------------------------------------------------------------
# Start application
# ---------------------------------------------------------------------------
start_app

# ---------------------------------------------------------------------------
# Cleanup decompressed file
# ---------------------------------------------------------------------------
if [[ "$CLEANUP_UNCOMPRESSED" -eq 1 ]]; then
  rm -f "$BACKUP_FILE"
  echo "[INFO] Removed temporary decompressed file."
fi

echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') Done."
