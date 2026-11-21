#!/bin/sh
# Run Maven with retries to handle transient repository/network failures.

set -eu

if [ "$#" -eq 0 ]; then
  echo "Usage: mvn-with-retry.sh <maven-args>" >&2
  exit 1
fi

attempt=1
max_attempts="${MAVEN_MAX_ATTEMPTS:-4}"
base_delay_seconds="${MAVEN_RETRY_DELAY_SECONDS:-15}"

while [ "$attempt" -le "$max_attempts" ]; do
  echo "Running Maven attempt ${attempt}/${max_attempts}: mvn $*"
  if mvn "$@"; then
    exit 0
  fi

  if [ "$attempt" -eq "$max_attempts" ]; then
    break
  fi

  delay_seconds=$((base_delay_seconds * attempt))
  echo "Maven command failed, retrying in ${delay_seconds}s..."
  sleep "$delay_seconds"
  attempt=$((attempt + 1))
done

echo "ERROR: Maven command failed after ${max_attempts} attempts" >&2
exit 1