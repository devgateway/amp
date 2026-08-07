#!/bin/bash
# entrypoint.sh — Patch config files with runtime env vars before Tomcat starts.
#
# Patches performed:
#   1. context.xml  — JDBC credentials (user, password, URL) from JDBC_* env vars
#   2. site-config.xml — buildSource entity from AMP_BUILD_LABEL env var (optional)
#
# Footer text ("AMP Togo...") is stored in the DB (DG_MESSAGE table).
# It is patched at container startup via psql if AMP_FOOTER_TEXT is set.
# The DB patch is done here via a background wait-and-patch so Tomcat can start
# while we wait for the DB to accept connections.
#
# Expected env vars:
#   JDBC_USER, JDBC_PASSWORD, JDBC_DB, JDBC_HOST, JDBC_PORT
#   AMP_BUILD_LABEL   (optional) — replaces the branch-name build tag in the footer
#   AMP_FOOTER_TEXT   (optional) — replaces the full footer text in the DB

set -euo pipefail

CONTEXT_XML="/usr/local/tomcat/webapps/ROOT/META-INF/context.xml"
SITE_CONFIG="/usr/local/tomcat/webapps/ROOT/TEMPLATE/ampTemplate/site-config.xml"

# ── 1. Patch context.xml (JDBC credentials) ──────────────────────────────────
if [[ -f "${CONTEXT_XML}" ]]; then
  echo "[entrypoint] Patching context.xml with runtime JDBC credentials..."
  sed -i \
    -e "s|username=\"[^\"]*\"|username=\"${JDBC_USER:-amp}\"|g" \
    -e "s|password=\"[^\"]*\"|password=\"${JDBC_PASSWORD:-amp}\"|g" \
    "${CONTEXT_XML}"
  sed -i \
    -e "s|jdbc:postgresql://[^/]*/[^\"]*|jdbc:postgresql://${JDBC_HOST:-db}:${JDBC_PORT:-5432}/${JDBC_DB:-amp}|g" \
    "${CONTEXT_XML}"
  echo "[entrypoint] context.xml patched."
else
  echo "[entrypoint] WARNING: ${CONTEXT_XML} not found — skipping JDBC patch."
fi

# ── 2. Patch site-config.xml (build label) ───────────────────────────────────
if [[ -n "${AMP_BUILD_LABEL:-}" ]] && [[ -f "${SITE_CONFIG}" ]]; then
  echo "[entrypoint] Setting buildSource to: ${AMP_BUILD_LABEL}"
  sed -i "s|<!ENTITY buildSource \"[^\"]*\">|<!ENTITY buildSource \"${AMP_BUILD_LABEL}\">|" "${SITE_CONFIG}"
  echo "[entrypoint] site-config.xml patched."
fi

# ── 3. Patch footer text in DB (background — waits for DB to be ready) ───────
if [[ -n "${AMP_FOOTER_TEXT:-}" ]]; then
  (
    DB_HOST="${JDBC_HOST:-db}"
    DB_PORT="${JDBC_PORT:-5432}"
    DB_NAME="${JDBC_DB:-amp}"
    DB_USER="${JDBC_USER:-amp}"
    PGPASSWORD="${JDBC_PASSWORD:-amp}"
    export PGPASSWORD

    echo "[entrypoint] Waiting for DB to accept connections before patching footer text..."
    for i in $(seq 1 30); do
      if psql -h "${DB_HOST}" -p "${DB_PORT}" -U "${DB_USER}" -d "${DB_NAME}" -c "SELECT 1" &>/dev/null; then
        echo "[entrypoint] DB ready. Updating footer text in DG_MESSAGE..."
        # Update all locales for the footer key. Uses DO ... to upsert cleanly.
        psql -h "${DB_HOST}" -p "${DB_PORT}" -U "${DB_USER}" -d "${DB_NAME}" <<SQL
UPDATE dg_message
  SET message_utf8 = '${AMP_FOOTER_TEXT}',
      orig_message = '${AMP_FOOTER_TEXT}'
WHERE message_key = 'Developed in partnership with OECD, UNDP, WB, Government of Ethiopia and DG';
SQL
        echo "[entrypoint] Footer text updated."
        break
      fi
      sleep 2
    done
  ) &
fi

# ── Hand off to Tomcat ────────────────────────────────────────────────────────
exec catalina.sh run
