#!/bin/sh
# npm install with retry logic
# Usage: npm-install-with-retry.sh [working-directory]
# If working-directory is provided, cd to it before running npm ci

set -e

WORK_DIR="${1:-.}"

# Configure npm retry settings
npm config set fetch-retries 5
npm config set fetch-retry-mintimeout 20000
npm config set fetch-retry-maxtimeout 120000
npm config set fetch-timeout 300000

# Retry logic with exponential backoff
cd "$WORK_DIR"
for delay in 0 10 20; do
  if [ "$delay" -gt 0 ]; then
    sleep "$delay"
  fi
  if npm ci; then
    exit 0
  fi
done

# If all retries failed, exit with error
echo "ERROR: npm ci failed after all retry attempts" >&2
exit 1

