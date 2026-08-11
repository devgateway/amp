# scripts/

Operational scripts for AMP database backup and restore.

## Files

| Script | Purpose |
| --- | --- |
| `backup.sh` | Create a PostgreSQL backup of the AMP database. |
| `restore.sh` | Restore the AMP database from a backup file. |
| `backup-cron.sh` | Wrapper for `backup.sh` that also prunes old backups; intended for cron or a systemd timer. |
| `extract-db-evidence.sh` | Export ISO 27001 audit evidence (users, roles, audit log, DB stats) from the AMP PostgreSQL container. |

## Configuration

All scripts read configuration from environment variables. Never hardcode credentials.

| Variable | Default | Description |
| --- | --- | --- |
| `AMP_DB_HOST` | `localhost` | PostgreSQL host |
| `AMP_DB_PORT` | `5432` | PostgreSQL port |
| `AMP_DB_NAME` | `amp` | Database name |
| `AMP_DB_USER` | `amp` | Database user |
| `PGPASSWORD` | — | **Required.** Password for `AMP_DB_USER`. |
| `AMP_BACKUP_DIR` | `/backups` | Directory for backup files |
| `AMP_DB_SUPERUSER` | `postgres` | Superuser for drop/create (restore only) |
| `PGPASSWORD_SUPER` | same as `PGPASSWORD` | Superuser password (restore only) |
| `AMP_CONTAINER` | `amp` | Docker container name (restore `--docker` mode) |
| `AMP_TOMCAT_HOME` | `/opt/tomcat` | Tomcat base directory (restore `--baremetal` mode) |
| `AMP_RETAIN_DAILY` | `7` | Days to keep daily backups (cron wrapper) |
| `AMP_RETAIN_WEEKLY` | `4` | Weeks to keep weekly backups (cron wrapper) |

## Quick Start

### One-off backup

```bash
export PGPASSWORD='your_password'
export AMP_BACKUP_DIR=/backups
./scripts/backup.sh
```

Produces `/backups/amp_amp_YYYYMMDD_HHMMSS.backup.7z`.

Use `--full` to include Jackrabbit `rep*` tables (file attachments):

```bash
./scripts/backup.sh --full
```

### Daily cron (root crontab)

```cron
0 2 * * * PGPASSWORD='your_password' AMP_BACKUP_DIR=/backups \
          /opt/amp/scripts/backup-cron.sh >> /var/log/amp-backup.log 2>&1
```

### Restore (Docker deployment)

```bash
export PGPASSWORD='your_password'
./scripts/restore.sh /backups/amp_amp_20260616_020001.backup.7z --docker
```

### Restore (bare-metal / Tomcat deployment)

```bash
export PGPASSWORD='your_password'
export AMP_TOMCAT_HOME=/opt/tomcat
./scripts/restore.sh /backups/amp_amp_20260616_020001.backup.7z --baremetal
```

> **Caution:** `restore.sh` drops and recreates the target database. Always run it
> against a test or recovery environment unless performing an authorized production restore.

## Dependencies

- `pg_dump` / `pg_restore` / `psql` — from `postgresql-client`
- `7za` — from `p7zip-full` (strongly recommended for compression)
- `docker` — only needed for `--docker` restore mode

---

## Extract ISO 27001 Evidence

```bash
./scripts/extract-db-evidence.sh
```

Connects to the default container (`amp-togo-pr-4454.de.ampsite.net-db`) and writes four files to `iso27001-evidence/access-reviews/evidence/`:

| Output file | Contents |
| --- | --- |
| `users-and-roles.csv` | All active users with workspace, role, last login, admin flag. |
| `admin-users.csv` | Global admins and team leads only. |
| `audit-log-sample.csv` | Most recent 200 AMP audit entries. |
| `trubudget-permissions.csv` | TruBudget intents assigned per user (blockchain permissions). |
| `db-stats.txt` | DB size, key table row counts, PostgreSQL roles, dormant accounts, TruBudget intent summary. |

Override defaults with flags:

```bash
./scripts/extract-db-evidence.sh \
  -c my-container \
  -d amp \
  -u amp \
  -o /tmp/evidence
```

Review output before sharing: mask IP addresses and remove personal data fields not needed by the auditor.

