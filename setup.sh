#!/usr/bin/env bash
# setup.sh — Install AMP + TruBudget using Docker
#
# Usage:
#   ./setup.sh [--no-trubudget] [--skip-build] [--down] [--down-all] [--help]
#
# Flags:
#   --no-trubudget   Start only AMP + PostgreSQL (skip TruBudget)
#   --skip-build     Do not rebuild the AMP Docker image
#   --down           Stop AMP containers (keeps volumes); also stops TruBudget
#   --down-all       Stop all containers AND remove volumes (data loss!)
#   --help           Show this message

set -euo pipefail

# ── Colours ───────────────────────────────────────────────────────────────────
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
NC='\033[0m'

log()  { echo -e "${GREEN}[setup]${NC} $*"; }
warn() { echo -e "${YELLOW}[warn]${NC}  $*"; }
err()  { echo -e "${RED}[error]${NC} $*" >&2; }

# ── Paths ─────────────────────────────────────────────────────────────────────
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ENV_FILE="${SCRIPT_DIR}/.env"
COMPOSE_FILE="${SCRIPT_DIR}/docker-compose.yml"
TRUBUDGET_DIR="${SCRIPT_DIR}/trubudget"
TRUBUDGET_OP_DIR="${TRUBUDGET_DIR}/scripts/operation"

# ── Defaults ──────────────────────────────────────────────────────────────────
WITH_TRUBUDGET=true
SKIP_BUILD=false
TEARDOWN=false
TEARDOWN_VOLUMES=false

# ── Argument parsing ──────────────────────────────────────────────────────────
for arg in "$@"; do
  case "$arg" in
    --no-trubudget)  WITH_TRUBUDGET=false ;;
    --skip-build)    SKIP_BUILD=true ;;
    --down)          TEARDOWN=true ;;
    --down-all)      TEARDOWN=true; TEARDOWN_VOLUMES=true ;;
    --help|-h)
      sed -n '/^# Usage:/,/^[^#]/p' "$0" | grep '^#' | sed 's/^# //'
      exit 0
      ;;
    *)
      err "Unknown argument: $arg"
      exit 1
      ;;
  esac
done

# ── Prerequisite checks ───────────────────────────────────────────────────────
check_cmd() {
  if ! command -v "$1" &>/dev/null; then
    err "'$1' is not installed or not in PATH. Please install it first."
    exit 1
  fi
}

log "Checking prerequisites..."
check_cmd docker
check_cmd git

if docker compose version &>/dev/null 2>&1; then
  DC="docker compose"
elif command -v docker-compose &>/dev/null; then
  DC="docker-compose"
else
  err "Neither 'docker compose' plugin nor 'docker-compose' found."
  exit 1
fi

# ── Env file ──────────────────────────────────────────────────────────────────
if [[ ! -f "${ENV_FILE}" ]]; then
  if [[ -f "${SCRIPT_DIR}/amp/.env.example" ]]; then
    warn ".env not found — copying from amp/.env.example"
    cp "${SCRIPT_DIR}/amp/.env.example" "${ENV_FILE}"
    warn "Edit ${ENV_FILE} — set SERVER_IP and all change_me_* values — then re-run."
    exit 1
  else
    warn ".env not found. Continuing with defaults."
  fi
fi

# Load env so we can use vars in this script
set -o allexport
# shellcheck disable=SC1090
[[ -f "${ENV_FILE}" ]] && source "${ENV_FILE}"
set +o allexport

# ── Tear-down path ────────────────────────────────────────────────────────────
if [[ "${TEARDOWN}" == true ]]; then
  log "Stopping AMP containers..."
  if [[ "${TEARDOWN_VOLUMES}" == true ]]; then
    warn "Removing AMP volumes (data will be lost)!"
    $DC -f "${COMPOSE_FILE}" --env-file "${ENV_FILE}" down -v --remove-orphans
  else
    $DC -f "${COMPOSE_FILE}" --env-file "${ENV_FILE}" down --remove-orphans
  fi

  if [[ -f "${TRUBUDGET_OP_DIR}/start-trubudget.sh" ]]; then
    log "Stopping TruBudget containers..."
    docker compose -f "${TRUBUDGET_OP_DIR}/docker-compose.yml" \
      -p trubudget-operation down --remove-orphans || true
  fi
  log "Done."
  exit 0
fi

# ── Create host log directories ───────────────────────────────────────────────
LOG_DIRS=(/var/log/amp /var/log/trubudget-blockchain /var/log/trubudget-api /var/log/trubudget-frontend)
log "Creating host log directories..."
for dir in "${LOG_DIRS[@]}"; do
  if [[ ! -d "${dir}" ]]; then
    sudo mkdir -p "${dir}" && sudo chmod 755 "${dir}"
    log "  Created ${dir}"
  else
    log "  Already exists: ${dir}"
  fi
done

# ── TruBudget: clone official repo ───────────────────────────────────────────
if [[ "${WITH_TRUBUDGET}" == true ]]; then
  TRUBUDGET_REPO="${TRUBUDGET_REPO:-https://github.com/openkfw/TruBudget.git}"
  TRUBUDGET_TAG="${TRUBUDGET_TAG:-main}"

  if [[ ! -d "${TRUBUDGET_DIR}/.git" ]]; then
    log "Cloning TruBudget repository (${TRUBUDGET_TAG})..."
    git clone --depth 1 --branch "${TRUBUDGET_TAG}" "${TRUBUDGET_REPO}" "${TRUBUDGET_DIR}"
  else
    log "TruBudget repo already cloned at ${TRUBUDGET_DIR}"
    log "  To update: cd ${TRUBUDGET_DIR} && git pull"
  fi

  # ── TruBudget: write their .env ─────────────────────────────────────────────
  log "Writing TruBudget .env from our settings..."
  TRUBUDGET_ENV="${TRUBUDGET_OP_DIR}/.env"

  # Start from their example so all their internal flags are present
  cp "${TRUBUDGET_OP_DIR}/.env.example" "${TRUBUDGET_ENV}"

  # Override with values from our .env
  # Use perl (available on all Linux/macOS) to replace in-place
  set_env() {
    local key="$1" val="$2"
    if grep -q "^${key}=" "${TRUBUDGET_ENV}"; then
      perl -pi -e "s|^${key}=.*|${key}=${val}|g" "${TRUBUDGET_ENV}"
    else
      echo "${key}=${val}" >> "${TRUBUDGET_ENV}"
    fi
  }

  set_env "ORGANIZATION"              "${TRUBUDGET_ORG:-AMP_ORG}"
  set_env "ORGANIZATION_VAULT_SECRET" "${TRUBUDGET_VAULT_SECRET:-change_me_vault_secret}"
  set_env "ROOT_SECRET"               "${TRUBUDGET_ROOT_SECRET:-change_me_root_secret}"
  set_env "MULTICHAIN_RPC_PASSWORD"   "${TRUBUDGET_RPC_PASSWORD:-change_me_rpc_password}"
  set_env "API_PORT"                  "${TRUBUDGET_API_PORT:-8081}"
  set_env "FRONTEND_PORT"             "${TRUBUDGET_UI_PORT:-3000}"
  set_env "TAG"                       "${TRUBUDGET_VERSION:-latest}"
  set_env "NODE_ENV"                  "production"

  # ── TruBudget: start via official script ────────────────────────────────────
  log "Starting TruBudget (slim: blockchain + api + frontend)..."
  bash "${TRUBUDGET_OP_DIR}/start-trubudget.sh" --slim --no-log
  log "TruBudget started."
fi

# ── ECR login & pull AMP image ───────────────────────────────────────────────
if [[ -z "${AMP_IMAGE:-}" || -z "${AMP_TAG:-}" ]]; then
  err "AMP_IMAGE and AMP_TAG must be set in ${ENV_FILE}"
  err "They are set by the GitHub Actions CI pipeline (build-push-ecr.yml)."
  exit 1
fi

if [[ -z "${AWS_ACCESS_KEY_ID:-}" || -z "${AWS_SECRET_ACCESS_KEY:-}" || -z "${AWS_DEFAULT_REGION:-}" ]]; then
  err "AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY, and AWS_DEFAULT_REGION must be set in ${ENV_FILE}"
  exit 1
fi

if [[ "${SKIP_BUILD}" == false ]]; then
  if ! command -v aws &>/dev/null; then
    err "'aws' CLI is not installed. Install it from https://docs.aws.amazon.com/cli/latest/userguide/install-cliv2.html"
    exit 1
  fi

  # Extract the ECR registry hostname (everything before the first /)
  ECR_REGISTRY=$(echo "${AMP_IMAGE}" | cut -d'/' -f1)

  log "Authenticating with ECR (${ECR_REGISTRY})..."
  aws ecr get-login-password --region "${AWS_DEFAULT_REGION}" \
    | docker login --username AWS --password-stdin "${ECR_REGISTRY}"

  log "Pulling AMP image: ${AMP_IMAGE}:${AMP_TAG}"
  docker pull "${AMP_IMAGE}:${AMP_TAG}"
else
  log "Skipping image pull (--skip-build)"
fi

# ── Start AMP ─────────────────────────────────────────────────────────────────
log "Starting AMP + PostgreSQL..."
$DC -f "${COMPOSE_FILE}" --env-file "${ENV_FILE}" up -d --remove-orphans

# ── Wait for AMP ─────────────────────────────────────────────────────────────
log "Waiting for AMP to be reachable..."
AMP_PORT="${AMP_PORT:-8080}"
RETRIES=60
WAIT=5
for i in $(seq 1 $RETRIES); do
  if curl -sf "http://localhost:${AMP_PORT}/ping" &>/dev/null; then
    log "AMP is up."
    break
  fi
  echo -ne "${CYAN}  attempt ${i}/${RETRIES} ...${NC}\r"
  sleep $WAIT
  if [[ $i -eq $RETRIES ]]; then
    warn "AMP did not respond within $((RETRIES * WAIT))s. Check logs:"
    warn "  $DC -f ${COMPOSE_FILE} logs -f amp"
  fi
done

# ── Summary ───────────────────────────────────────────────────────────────────
SERVER_IP="${SERVER_IP:-<your-server-ip>}"
AMP_PORT="${AMP_PORT:-8080}"
TRUBUDGET_UI_PORT="${TRUBUDGET_UI_PORT:-3000}"
TRUBUDGET_API_PORT="${TRUBUDGET_API_PORT:-8081}"

echo ""
log "Stack is running."
echo -e "  AMP:              ${CYAN}http://${SERVER_IP}:${AMP_PORT}${NC}"
if [[ "${WITH_TRUBUDGET}" == true ]]; then
  echo -e "  TruBudget UI:     ${CYAN}http://${SERVER_IP}:${TRUBUDGET_UI_PORT}${NC}"
  echo -e "  TruBudget API:    ${CYAN}http://${SERVER_IP}:${TRUBUDGET_API_PORT}/api${NC}"
fi
echo ""
log "Useful commands:"
echo "  AMP logs:          $DC -f ${COMPOSE_FILE} logs -f amp"
echo "  TruBudget logs:    docker compose -f ${TRUBUDGET_OP_DIR}/docker-compose.yml logs -f"
echo "  Stop all:          ./setup.sh --down"
echo "  Remove all data:   ./setup.sh --down-all"
echo ""
log "After the stack is up, configure TruBudget in the AMP admin panel:"
echo "  Admin > Global Settings > trubudget > baseUrl"
echo "  Value: http://host.docker.internal:${TRUBUDGET_API_PORT}/"
