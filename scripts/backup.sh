#!/usr/bin/env bash
# =============================================================================
# AMP Database Backup
# =============================================================================
# Usage:
#   ./backup.sh [--full]
#
#   --full  Include Jackrabbit rep* tables (large; omit unless file attachments
#           are required in the backup).
#
# Configuration — set as environment variables or export from a .env file:
#
#   AMP_DB_HOST     (default: localhost)
#   AMP_DB_PORT     (default: 5432)
#   AMP_DB_NAME     (default: amp)
#   AMP_DB_USER     (default: amp)
#   PGPASSWORD      PostgreSQL password. Never hardcode it here.
#   AMP_BACKUP_DIR  Directory to write backup files (default: /backups)
#
# Output:
#   <AMP_BACKUP_DIR>/amp_<DBNAME>_YYYYMMDD_HHMMSS.backup          raw dump
#   <AMP_BACKUP_DIR>/amp_<DBNAME>_YYYYMMDD_HHMMSS.backup.7z       compressed
#   The raw dump is removed after compression unless 7za is unavailable.
#
# Exit codes:
#   0  success
#   1  configuration or dependency error
#   2  pg_dump failed
#   3  compression failed
# =============================================================================

set -euo pipefail

# ---------------------------------------------------------------------------
# Configuration
# ---------------------------------------------------------------------------
AMP_DB_HOST="${AMP_DB_HOST:-localhost}"
AMP_DB_PORT="${AMP_DB_PORT:-5432}"
AMP_DB_NAME="${AMP_DB_NAME:-amp}"
AMP_DB_USER="${AMP_DB_USER:-amp}"
AMP_BACKUP_DIR="${AMP_BACKUP_DIR:-/backups}"

FULL_BACKUP=0
for arg in "$@"; do
  [[ "$arg" == "--full" ]] && FULL_BACKUP=1
done

# ---------------------------------------------------------------------------
# Dependency checks
# ---------------------------------------------------------------------------
if ! command -v pg_dump >/dev/null 2>&1; then
  echo "[ERROR] pg_dump not found. Install postgresql-client." >&2
  exit 1
fi

if [[ -z "${PGPASSWORD:-}" ]]; then
  echo "[ERROR] PGPASSWORD is not set. Export it before running this script." >&2
  echo "        Example: export PGPASSWORD='your_password'" >&2
  exit 1
fi

# ---------------------------------------------------------------------------
# Prepare output directory
# ---------------------------------------------------------------------------
mkdir -p "$AMP_BACKUP_DIR"
chmod 700 "$AMP_BACKUP_DIR"

TIMESTAMP=$(date +%Y%m%d_%H%M%S)
BACKUP_FILE="${AMP_BACKUP_DIR}/amp_${AMP_DB_NAME}_${TIMESTAMP}.backup"

# ---------------------------------------------------------------------------
# Run backup
# ---------------------------------------------------------------------------
echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') Starting backup of ${AMP_DB_NAME} on ${AMP_DB_HOST}:${AMP_DB_PORT}"

if [[ "$FULL_BACKUP" -eq 1 ]]; then
  echo "[INFO] Mode: FULL (includes Jackrabbit rep* tables)"
  pg_dump \
    --host="$AMP_DB_HOST" \
    --port="$AMP_DB_PORT" \
    --username="$AMP_DB_USER" \
    --no-password \
    --format=tar \
    --blobs \
    --file="$BACKUP_FILE" \
    "$AMP_DB_NAME"
else
  echo "[INFO] Mode: STANDARD (excludes Jackrabbit rep* tables)"
  pg_dump \
    --host="$AMP_DB_HOST" \
    --port="$AMP_DB_PORT" \
    --username="$AMP_DB_USER" \
    --no-password \
    --format=tar \
    --exclude-table-data='rep*' \
    -T 'rep*' \
    --blobs \
    --file="$BACKUP_FILE" \
    "$AMP_DB_NAME"
fi

if [[ $? -ne 0 ]]; then
  echo "[ERROR] pg_dump failed. Backup file: ${BACKUP_FILE}" >&2
  exit 2
fi

BACKUP_SIZE=$(du -sh "$BACKUP_FILE" | cut -f1)
echo "[INFO] Backup written: ${BACKUP_FILE} (${BACKUP_SIZE})"

# ---------------------------------------------------------------------------
# Compress
# ---------------------------------------------------------------------------
if command -v 7za >/dev/null 2>&1; then
  echo "[INFO] Compressing with 7za..."
  7za a -mx=9 "${BACKUP_FILE}.7z" "$BACKUP_FILE" >/dev/null
  if [[ $? -ne 0 ]]; then
    echo "[ERROR] Compression failed. Raw backup kept at: ${BACKUP_FILE}" >&2
    exit 3
  fi
  rm "$BACKUP_FILE"
  COMPRESSED_SIZE=$(du -sh "${BACKUP_FILE}.7z" | cut -f1)
  echo "[INFO] Compressed:   ${BACKUP_FILE}.7z (${COMPRESSED_SIZE})"
else
  echo "[WARN] 7za not found. Keeping uncompressed backup: ${BACKUP_FILE}"
  echo "[WARN] Install p7zip-full for compression: apt install p7zip-full"
fi

# ---------------------------------------------------------------------------
# Summary
# ---------------------------------------------------------------------------
echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') Backup complete."
