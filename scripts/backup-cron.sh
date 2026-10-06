#!/usr/bin/env bash
# =============================================================================
# AMP Scheduled Backup — intended for use by cron or a systemd timer.
# =============================================================================
# Runs backup.sh and keeps a rolling set of backup archives.
#
# Retention (configurable via env vars):
#   AMP_RETAIN_DAILY    Days to keep daily backups (default: 7)
#   AMP_RETAIN_WEEKLY   Weeks to keep Sunday backups (default: 4)
#
# Recommended crontab entry (runs at 02:00 every day):
#   0 2 * * * PGPASSWORD='...' AMP_BACKUP_DIR=/backups \
#             /opt/amp/scripts/backup-cron.sh >> /var/log/amp-backup.log 2>&1
#
# All other configuration is read from the same env vars as backup.sh.
# =============================================================================

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
AMP_BACKUP_DIR="${AMP_BACKUP_DIR:-/backups}"
AMP_RETAIN_DAILY="${AMP_RETAIN_DAILY:-7}"
AMP_RETAIN_WEEKLY="${AMP_RETAIN_WEEKLY:-4}"

# Run the backup
"${SCRIPT_DIR}/backup.sh" "$@"

# ---------------------------------------------------------------------------
# Prune old backups
# Keeps:
#   - all backups younger than AMP_RETAIN_DAILY days
#   - one backup per week (Sunday) for AMP_RETAIN_WEEKLY weeks
# ---------------------------------------------------------------------------
echo "[INFO] Pruning backups older than ${AMP_RETAIN_DAILY} days in ${AMP_BACKUP_DIR} ..."

DOW=$(date +%u)   # 1=Mon … 7=Sun

if [[ "$DOW" -eq 7 ]]; then
  # Today is Sunday — keep this week's archive indefinitely until the weekly
  # retention window expires; only delete weeklies older than the window.
  find "$AMP_BACKUP_DIR" -maxdepth 1 -name 'amp_*.backup.7z' \
    -mtime "+$((AMP_RETAIN_WEEKLY * 7))" -delete
else
  # Delete daily backups outside the daily retention window.
  find "$AMP_BACKUP_DIR" -maxdepth 1 -name 'amp_*.backup.7z' \
    -mtime "+${AMP_RETAIN_DAILY}" -delete
fi

echo "[INFO] Pruning complete."
