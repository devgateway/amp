#!/bin/bash
# entrypoint.sh — Patch context.xml with runtime JDBC credentials before Tomcat starts.
#
# The JDBC credentials are baked into META-INF/context.xml at build time by the
# Maven XSLT transformation.  This script overwrites those baked-in values with
# whatever is set in the container's environment (from docker-compose / .env),
# so the same image can be deployed against any database without a rebuild.
#
# Expected env vars (all optional — defaults are safe fallbacks):
#   JDBC_USER      (default: amp)
#   JDBC_PASSWORD  (default: amp)
#   JDBC_DB        (default: amp)
#   JDBC_HOST      (default: db)
#   JDBC_PORT      (default: 5432)

set -euo pipefail

CONTEXT_XML="/usr/local/tomcat/webapps/ROOT/META-INF/context.xml"

if [[ -f "${CONTEXT_XML}" ]]; then
  echo "[entrypoint] Patching ${CONTEXT_XML} with runtime JDBC credentials..."

  # Use | as sed delimiter to avoid issues if values contain /
  sed -i \
    -e "s|username=\"[^\"]*\"|username=\"${JDBC_USER:-amp}\"|g" \
    -e "s|password=\"[^\"]*\"|password=\"${JDBC_PASSWORD:-amp}\"|g" \
    "${CONTEXT_XML}"

  # Also patch the JDBC URL host, port, and database name
  # URL format: jdbc:postgresql://HOST:PORT/DB
  sed -i \
    -e "s|jdbc:postgresql://[^/]*/[^\"]*|jdbc:postgresql://${JDBC_HOST:-db}:${JDBC_PORT:-5432}/${JDBC_DB:-amp}|g" \
    "${CONTEXT_XML}"

  echo "[entrypoint] context.xml patched."
else
  echo "[entrypoint] WARNING: ${CONTEXT_XML} not found — skipping patch."
fi

# Hand off to the original Tomcat startup
exec catalina.sh run
