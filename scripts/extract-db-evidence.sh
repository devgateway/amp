#!/usr/bin/env bash
# =============================================================================
# AMP Database Evidence Extractor
# =============================================================================
# Connects to the AMP PostgreSQL container and exports redacted evidence files
# needed for an ISO 27001 access review and audit log sample.
#
# Usage:
#   ./extract-db-evidence.sh [options]
#
# Options:
#   -c CONTAINER   Docker container name (default: amp-togo-pr-4454.de.ampsite.net-db)
#   -d DATABASE    Database name           (default: amp)
#   -u DB_USER     PostgreSQL user         (default: amp)
#   -o OUTPUT_DIR  Output directory        (default: ./iso27001-evidence/access-reviews/evidence)
#   -h             Show this help
#
# Output files (all in OUTPUT_DIR):
#   users-and-roles.csv       All active users, their workspace roles, admin flag, last login.
#   admin-users.csv           Users with global_admin=true or team-head role.
#   audit-log-sample.csv      Most recent 200 AMP audit entries.
#   trubudget-permissions.csv TruBudget intents (blockchain permissions) assigned per user.
#   db-stats.txt              Database size, table row counts, PostgreSQL role list, TruBudget intent summary.
#
# No passwords are written to any output file.
# =============================================================================

set -euo pipefail

# ---------------------------------------------------------------------------
# Defaults
# ---------------------------------------------------------------------------
CONTAINER="amp-togo-pr-4454.de.ampsite.net-db"
DATABASE="amp"
DB_USER="amp"
OUTPUT_DIR="/tmp/evidence"

# ---------------------------------------------------------------------------
# Argument parsing
# ---------------------------------------------------------------------------
while getopts "c:d:u:o:h" opt; do
  case "$opt" in
    c) CONTAINER="$OPTARG" ;;
    d) DATABASE="$OPTARG" ;;
    u) DB_USER="$OPTARG" ;;
    o) OUTPUT_DIR="$OPTARG" ;;
    h)
      sed -n '3,25p' "$0" | sed 's/^# \{0,2\}//'
      exit 0
      ;;
    *) echo "Unknown option. Run with -h for help." >&2; exit 1 ;;
  esac
done

# ---------------------------------------------------------------------------
# Pre-flight checks
# ---------------------------------------------------------------------------
if ! command -v docker >/dev/null 2>&1; then
  echo "[ERROR] docker not found." >&2; exit 1
fi

if ! docker inspect "$CONTAINER" >/dev/null 2>&1; then
  echo "[ERROR] Container not found or not running: ${CONTAINER}" >&2; exit 1
fi

mkdir -p "$OUTPUT_DIR"
chmod 700 "$OUTPUT_DIR"

TIMESTAMP=$(date +%Y%m%d_%H%M%S)
echo "[INFO] $(date '+%Y-%m-%d %H:%M:%S') Extracting evidence from container: ${CONTAINER}"
echo "[INFO] Database: ${DATABASE}  User: ${DB_USER}  Output: ${OUTPUT_DIR}"
echo

# ---------------------------------------------------------------------------
# Helper: run a SQL query inside the container via psql
# ---------------------------------------------------------------------------
run_psql() {
  # $1 = SQL string
  docker exec "$CONTAINER" \
    psql --username="$DB_USER" --dbname="$DATABASE" \
    --no-password --csv \
    --command="$1"
}

run_psql_text() {
  # $1 = SQL string — plain aligned text output (for stats)
  docker exec "$CONTAINER" \
    psql --username="$DB_USER" --dbname="$DATABASE" \
    --no-password \
    --command="$1"
}

# ---------------------------------------------------------------------------
# 1. All active users with workspace roles and last login
#    Excludes password, salt, pass_answer columns.
# ---------------------------------------------------------------------------
echo "[INFO] Extracting: users and workspace roles ..."
cat <<'EOF' | run_psql "$(cat)" > "${OUTPUT_DIR}/users-and-roles.csv"
SELECT
    u.id                                      AS user_id,
    u.first_names || ' ' || u.last_name       AS full_name,
    u.email,
    CASE WHEN u.banned THEN 'yes' ELSE 'no' END  AS banned,
    CASE WHEN u.global_admin THEN 'yes' ELSE 'no' END AS global_admin,
    t.name                                    AS workspace,
    r.role                                    AS workspace_role,
    CASE WHEN r.team_head THEN 'yes' ELSE 'no' END AS is_team_lead,
    CASE WHEN r.approver  THEN 'yes' ELSE 'no' END AS is_approver,
    TO_CHAR(li.last_visit, 'YYYY-MM-DD')      AS last_login,
    TO_CHAR(u.password_changed_at, 'YYYY-MM-DD') AS password_changed_at
FROM dg_user u
LEFT JOIN amp_team_member tm ON tm.user_ = u.id AND (tm.deleted IS NULL OR tm.deleted = false)
LEFT JOIN amp_team          t  ON t.amp_team_id = tm.amp_team_id
LEFT JOIN amp_team_member_roles r ON r.amp_team_mem_role_id = tm.amp_member_role_id
LEFT JOIN dg_user_login_info li  ON li.id = u.id
WHERE u.banned = false
ORDER BY u.email, t.name;
EOF
echo "[INFO] Written: users-and-roles.csv ($(wc -l < "${OUTPUT_DIR}/users-and-roles.csv") rows)"

# ---------------------------------------------------------------------------
# 2. Privileged users only (global admins + team leads)
# ---------------------------------------------------------------------------
echo "[INFO] Extracting: privileged users ..."
cat <<'EOF' | run_psql "$(cat)" > "${OUTPUT_DIR}/admin-users.csv"
SELECT
    u.id                                      AS user_id,
    u.first_names || ' ' || u.last_name       AS full_name,
    u.email,
    CASE WHEN u.global_admin THEN 'yes' ELSE 'no' END AS global_admin,
    t.name                                    AS workspace,
    r.role                                    AS workspace_role,
    CASE WHEN r.team_head THEN 'yes' ELSE 'no' END AS is_team_lead,
    CASE WHEN r.approver  THEN 'yes' ELSE 'no' END AS is_approver,
    TO_CHAR(li.last_visit, 'YYYY-MM-DD')      AS last_login,
    TO_CHAR(u.password_changed_at, 'YYYY-MM-DD') AS password_changed_at
FROM dg_user u
LEFT JOIN amp_team_member tm ON tm.user_ = u.id AND (tm.deleted IS NULL OR tm.deleted = false)
LEFT JOIN amp_team          t  ON t.amp_team_id = tm.amp_team_id
LEFT JOIN amp_team_member_roles r ON r.amp_team_mem_role_id = tm.amp_member_role_id
LEFT JOIN dg_user_login_info    li ON li.id = u.id
WHERE u.banned = false
  AND (u.global_admin = true OR r.team_head = true)
ORDER BY u.global_admin DESC, u.email;
EOF
echo "[INFO] Written: admin-users.csv ($(wc -l < "${OUTPUT_DIR}/admin-users.csv") rows)"

# ---------------------------------------------------------------------------
# 3. Audit log sample — most recent 200 entries
#    IP addresses included for audit; mask before sharing externally.
# ---------------------------------------------------------------------------
echo "[INFO] Extracting: audit log sample (last 200 entries) ..."
run_psql "
SELECT
    TO_CHAR(al.loggeddate, 'YYYY-MM-DD HH24:MI:SS') AS logged_at,
    al.authoremail   AS author_email,
    al.editoremail   AS editor_email,
    al.action,
    al.objecttype    AS object_type,
    al.objectname    AS object_name,
    al.teamname      AS workspace,
    al.ip
FROM amp_audit_logger al
ORDER BY al.loggeddate DESC
LIMIT 200;
" > "${OUTPUT_DIR}/audit-log-sample.csv"
echo "[INFO] Written: audit-log-sample.csv ($(wc -l < "${OUTPUT_DIR}/audit-log-sample.csv") rows)"

# ---------------------------------------------------------------------------
# 4. TruBudget blockchain permissions per user
#    amp_user_trubudget_intent links DG_USER to global/project/sub/wf intents.
# ---------------------------------------------------------------------------
echo "[INFO] Extracting: TruBudget permissions ..."
run_psql "
SELECT
    u.email,
    u.first_names || ' ' || u.last_name  AS full_name,
    CASE WHEN u.global_admin THEN 'yes' ELSE 'no' END AS global_admin,
    ti.trubudget_intent_name             AS intent,
    ti.trubudget_intent_display_name     AS intent_label
FROM dg_user u
JOIN amp_user_trubudget_intent uti ON uti.user_id = u.id
JOIN amp_trubudget_intent      ti  ON ti.trubudget_intent_id = uti.trubudget_intent_id
WHERE u.banned = false
ORDER BY u.email, ti.trubudget_intent_name;
" > "${OUTPUT_DIR}/trubudget-permissions.csv"
echo "[INFO] Written: trubudget-permissions.csv ($(wc -l < "${OUTPUT_DIR}/trubudget-permissions.csv") rows)"

# ---------------------------------------------------------------------------
# 5. Database stats — size, top table row counts, PostgreSQL roles
# ---------------------------------------------------------------------------
echo "[INFO] Extracting: database stats ..."
{
  echo "=== Extracted: ${TIMESTAMP} | Container: ${CONTAINER} | Database: ${DATABASE} ==="
  echo

  echo "--- Database size ---"
  run_psql_text "SELECT pg_size_pretty(pg_database_size('${DATABASE}')) AS db_size;"
  echo

  echo "--- Key table row counts ---"
  run_psql_text "
SELECT
    'dg_user'             AS table_name, COUNT(*) AS rows FROM dg_user
UNION ALL SELECT 'dg_user (active)',           COUNT(*) FROM dg_user WHERE banned = false
UNION ALL SELECT 'dg_user (global_admin)',      COUNT(*) FROM dg_user WHERE global_admin = true
UNION ALL SELECT 'amp_team',                   COUNT(*) FROM amp_team
UNION ALL SELECT 'amp_team_member',            COUNT(*) FROM amp_team_member
UNION ALL SELECT 'amp_team_member (active)',   COUNT(*) FROM amp_team_member WHERE deleted IS NULL OR deleted = false
UNION ALL SELECT 'amp_audit_logger',           COUNT(*) FROM amp_audit_logger
UNION ALL SELECT 'amp_audit_logger (30 days)', COUNT(*) FROM amp_audit_logger WHERE loggeddate > NOW() - INTERVAL '30 days'
ORDER BY 1;
"
  echo

  echo "--- PostgreSQL database roles ---"
  run_psql_text "
SELECT rolname, rolsuper, rolcreatedb, rolcreaterole, rolcanlogin,
       CASE WHEN rolvaliduntil IS NULL THEN 'no expiry' ELSE rolvaliduntil::text END AS valid_until
FROM pg_roles
ORDER BY rolsuper DESC, rolcanlogin DESC, rolname;
"
  echo

  echo "--- Users with no login in the last 90 days ---"
  run_psql_text "
SELECT u.email,
       TO_CHAR(li.last_visit, 'YYYY-MM-DD') AS last_login
FROM dg_user u
JOIN dg_user_login_info li ON li.id = u.id
WHERE u.banned = false
  AND li.last_visit < NOW() - INTERVAL '90 days'
ORDER BY li.last_visit;
"
  echo

  echo "--- TruBudget intent assignment summary ---"
  run_psql_text "
SELECT
    ti.trubudget_intent_name AS intent,
    COUNT(DISTINCT uti.user_id) AS users_assigned
FROM amp_trubudget_intent ti
LEFT JOIN amp_user_trubudget_intent uti ON uti.trubudget_intent_id = ti.trubudget_intent_id
GROUP BY ti.trubudget_intent_name
ORDER BY ti.trubudget_intent_name;
"
} > "${OUTPUT_DIR}/db-stats.txt"
echo "[INFO] Written: db-stats.txt"

# ---------------------------------------------------------------------------
# Summary
# ---------------------------------------------------------------------------
echo
echo "[INFO] Done. Output files:"
ls -lh "${OUTPUT_DIR}/"*.csv "${OUTPUT_DIR}/"*.txt 2>/dev/null | awk '{print "  " $NF, $5}'
echo
echo "[INFO] TruBudget permissions written to: trubudget-permissions.csv"
echo "       This discloses blockchain intents (create project, create user, grant/revoke etc.)."
echo "       Review before sharing with auditors — mask or drop the full_name column if needed."
echo
echo "[WARN] Review output before sharing:"
echo "       - Mask or remove IP addresses in audit-log-sample.csv if not required by auditors."
echo "       - Remove full_name column from exports if personal data minimisation applies."
echo "       - Store originals in approved secure evidence location, not in source control."
