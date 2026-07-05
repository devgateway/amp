#!/usr/bin/env bash
# backup.sh — Backup AMP PostgreSQL database and uploads volume
#
# Creates timestamped backups of:
#   - PostgreSQL database (pg_dump → gzipped SQL)
#   - AMP uploads volume  (tar.gz archive)
#
# Usage:
#   ./backup.sh [--db-only] [--uploads-only] [--keep <days>] [--dest <dir>] [--help]
#
# Flags:
#   --db-only         Only back up the database
#   --uploads-only    Only back up the uploads volume
#   --keep <days>     Delete backups older than N days (default: 30)
#   --dest <dir>      Directory to write backups to (default: /opt/amp/backups)
#   --help            Show this message
#
# To run automatically, add to crontab:
#   0 2 * * * /opt/amp/backup.sh >> /var/log/amp-backup.log 2>&1

set -euo pipefail

RED='\033[0;31m'; GREEN='\033[0;32m'; YELLOW='\033[1;33m'; CYAN='\033[0;36m'; NC='\033[0m'
log()  { echo -e "${GREEN}[backup]${NC} $(date '+%Y-%m-%d %H:%M:%S') $*"; }
warn() { echo -e "${YELLOW}[warn]${NC}   $(date '+%Y-%m-%d %H:%M:%S') $*"; }
err()  { echo -e "${RED}[error]${NC}  $(date '+%Y-%m-%d %H:%M:%S') $*" >&2; }

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ENV_FILE="${SCRIPT_DIR}/.env"

# ── Defaults ──────────────────────────────────────────────────────────────────
BACKUP_DEST="/opt/amp/backups"
KEEP_DAYS=30
DO_DB=true
DO_UPLOADS=true

# ── Args ──────────────────────────────────────────────────────────────────────
while [[ $# -gt 0 ]]; do
  case "$1" in
    --db-only)      DO_UPLOADS=false; shift ;;
    --uploads-only) DO_DB=false; shift ;;
    --keep)         KEEP_DAYS="$2"; shift 2 ;;
    --dest)         BACKUP_DEST="$2"; shift 2 ;;
    --help|-h)
      sed -n '/^# Usage:/,/^[^#]/p' "$0" | grep '^#' | sed 's/^# //'
      exit 0 ;;
    *) err "Unknown argument: $1"; exit 1 ;;
  esac
done

# ── Load .env ─────────────────────────────────────────────────────────────────
# Strip inline comments before sourcing — bash executes them otherwise.
if [[ -f "${ENV_FILE}" ]]; then
  while IFS= read -r line; do
    # Skip blank lines and full-line comments
    [[ -z "${line}" || "${line}" =~ ^[[:space:]]*# ]] && continue
    # Strip trailing inline comment (space + # + anything)
    line="${line%%  #*}"
    line="${line%% #*}"
    export "${line?}" 2>/dev/null || true
  done < <(grep -v '^[[:space:]]*#' "${ENV_FILE}" | grep '=')
fi

DB_NAME="${AMP_DB_NAME:-amp}"
DB_USER="${AMP_DB_USER:-amp}"
DB_PASSWORD="${AMP_DB_PASSWORD:-amp}"
DB_CONTAINER="${AMP_DB_CONTAINER:-amp-db}"

# ── Setup ─────────────────────────────────────────────────────────────────────
TIMESTAMP=$(date '+%Y%m%d_%H%M%S')
mkdir -p "${BACKUP_DEST}"

log "Starting backup (timestamp: ${TIMESTAMP})"
log "Destination: ${BACKUP_DEST}"

# ── Database backup ───────────────────────────────────────────────────────────
if [[ "${DO_DB}" == true ]]; then
  DB_BACKUP="${BACKUP_DEST}/amp-db_${TIMESTAMP}.sql.gz"
  log "Backing up database '${DB_NAME}' from container '${DB_CONTAINER}'..."

  if ! docker ps --format '{{.Names}}' | grep -q "^${DB_CONTAINER}$"; then
    err "Container '${DB_CONTAINER}' is not running. Is the stack up?"
    exit 1
  fi

  docker exec "${DB_CONTAINER}" \
    env PGPASSWORD="${DB_PASSWORD}" \
    pg_dump -U "${DB_USER}" -d "${DB_NAME}" --no-password \
    | gzip > "${DB_BACKUP}"

  SIZE=$(du -sh "${DB_BACKUP}" | cut -f1)
  log "Database backup complete: $(basename "${DB_BACKUP}") (${SIZE})"
fi

# ── Uploads volume backup ─────────────────────────────────────────────────────
if [[ "${DO_UPLOADS}" == true ]]; then
  UPLOADS_BACKUP="${BACKUP_DEST}/amp-uploads_${TIMESTAMP}.tar.gz"
  log "Backing up uploads volume..."

  # Use a temporary Alpine container to tar the volume without stopping AMP
  docker run --rm \
    -v amp_amp_uploads:/data:ro \
    -v "${BACKUP_DEST}":/backup \
    alpine \
    tar czf "/backup/amp-uploads_${TIMESTAMP}.tar.gz" -C /data .

  SIZE=$(du -sh "${UPLOADS_BACKUP}" | cut -f1)
  log "Uploads backup complete: $(basename "${UPLOADS_BACKUP}") (${SIZE})"
fi

# ── Prune old backups ─────────────────────────────────────────────────────────
if [[ "${KEEP_DAYS}" -gt 0 ]]; then
  log "Removing backups older than ${KEEP_DAYS} days..."
  find "${BACKUP_DEST}" -maxdepth 1 \
    \( -name "amp-db_*.sql.gz" -o -name "amp-uploads_*.tar.gz" \) \
    -mtime "+${KEEP_DAYS}" -print -delete
fi

# ── Summary ───────────────────────────────────────────────────────────────────
log "Backup finished."
echo ""
echo -e "  Backup directory: ${CYAN}${BACKUP_DEST}${NC}"
echo -e "  Contents:"
ls -lh "${BACKUP_DEST}" | grep -E "amp-(db|uploads)_" | awk '{print "    " $NF " (" $5 ")"}'
echo ""
echo "To restore the database:"
echo "  gunzip -c ${BACKUP_DEST}/amp-db_${TIMESTAMP}.sql.gz \\"
echo "    | docker exec -i ${DB_CONTAINER} env PGPASSWORD=${DB_PASSWORD} \\"
echo "    psql -U ${DB_USER} -d ${DB_NAME}"
