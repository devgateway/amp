#!/usr/bin/env bash
# setup.sh — Install AMP + TruBudget using Docker
#
# Usage:
#   ./setup.sh [--no-trubudget] [--full-trubudget] [--skip-build] [--down] [--down-all] [--background] [--lang=<code>] [--help]
#
# Flags:
#   --no-trubudget    Start only AMP + PostgreSQL (skip TruBudget)
#   --full-trubudget  Start all TruBudget services (default: slim — blockchain + api + frontend only)
#   --skip-build      Do not rebuild the AMP Docker image
#   --down            Stop AMP containers (keeps volumes); also stops TruBudget
#   --down-all        Stop all containers AND remove volumes (data loss!)
#   --background      Run the entire setup in the background; tail the log to follow progress
#   --lang=<code>     Log language: en (default), fr
#   --help            Show this message

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

# ── Translations ──────────────────────────────────────────────────────────────
# Usage:  t KEY [printf-args...]
# Returns the message for KEY in SETUP_LANG, falling back to English.
t() {
  local key="$1"; shift
  local tmpl
  case "${SETUP_LANG:-en}:${key}" in
    # ── French ────────────────────────────────────────────────────────────────
    fr:checking_prereqs)    tmpl="Vérification des prérequis..." ;;
    fr:creating_log_dirs)   tmpl="Création des répertoires de journaux hôtes..." ;;
    fr:dir_created)         tmpl="  Créé : %s" ;;
    fr:dir_exists)          tmpl="  Existe déjà : %s" ;;
    fr:tb_cloning)          tmpl="Clonage du dépôt TruBudget (%s)..." ;;
    fr:tb_cloned)           tmpl="Dépôt TruBudget déjà cloné dans %s" ;;
    fr:tb_update_hint)      tmpl="  Mise à jour : cd %s && git pull" ;;
    fr:tb_writing_env)      tmpl="Écriture du fichier .env de TruBudget depuis nos paramètres..." ;;
    fr:tb_running_no_ver)   tmpl="TruBudget est déjà en cours — version non détectable, ignoré pour préserver les données." ;;
    fr:tb_running_same)     tmpl="TruBudget est déjà en cours à la version '%s' — ignoré pour préserver les données." ;;
    fr:tb_ver_change)       tmpl="Changement de version TruBudget : en cours='%s' → configuré='%s'" ;;
    fr:tb_pulling)          tmpl="Téléchargement des nouvelles images TruBudget (services : %s)..." ;;
    fr:tb_restarting)       tmpl="Redémarrage de TruBudget avec les nouvelles images (volumes conservés)..." ;;
    fr:tb_upgraded)         tmpl="TruBudget mis à jour vers la version '%s'." ;;
    fr:tb_starting_full)    tmpl="Démarrage de TruBudget (complet — tous les services)..." ;;
    fr:tb_starting_slim)    tmpl="Démarrage de TruBudget (minimal : blockchain + api + frontend)..." ;;
    fr:tb_started)          tmpl="TruBudget démarré." ;;
    fr:tb_stopping)         tmpl="Arrêt des conteneurs TruBudget..." ;;
    fr:amp_image_missing)   tmpl="AMP_IMAGE et AMP_TAG doivent être définis dans %s" ;;
    fr:amp_image_ci_hint)   tmpl="Ils sont définis par le pipeline CI GitHub Actions (build-push-ecr.yml)." ;;
    fr:aws_creds_missing)   tmpl="AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY et AWS_DEFAULT_REGION doivent être définis dans %s" ;;
    fr:aws_installing)      tmpl="AWS CLI introuvable — installation en cours..." ;;
    fr:aws_installed)       tmpl="AWS CLI installé : %s" ;;
    fr:ecr_auth)            tmpl="Authentification auprès d'ECR (%s)..." ;;
    fr:amp_pulling)         tmpl="Téléchargement de l'image AMP : %s:%s" ;;
    fr:skip_pull)           tmpl="Téléchargement ignoré (--skip-build)" ;;
    fr:pulling_postgis)     tmpl="Téléchargement de l'image postgres+postgis..." ;;
    fr:starting_amp)        tmpl="Démarrage de AMP + PostgreSQL..." ;;
    fr:waiting_amp)         tmpl="En attente du démarrage de AMP..." ;;
    fr:amp_up)              tmpl="AMP est opérationnel." ;;
    fr:amp_timeout)         tmpl="AMP n'a pas répondu dans les %ss. Vérifiez les journaux :" ;;
    fr:amp_logs_hint)       tmpl="  Journaux AMP :           %s" ;;
    fr:tb_logs_hint)        tmpl="  Journaux TruBudget :     docker compose -f %s logs -f" ;;
    fr:stack_running)       tmpl="La pile est en cours d'exécution." ;;
    fr:useful_commands)     tmpl="Commandes utiles :" ;;
    fr:stop_hint)           tmpl="  Arrêter tout :           ./setup.sh --down" ;;
    fr:remove_hint)         tmpl="  Supprimer toutes données: ./setup.sh --down-all" ;;
    fr:tb_config_hint)      tmpl="Configurez TruBudget dans le panneau d'administration AMP après le démarrage :" ;;
    fr:stopping_amp)        tmpl="Arrêt des conteneurs AMP..." ;;
    fr:removing_volumes)    tmpl="Suppression des volumes AMP (les données seront perdues) !" ;;
    fr:done)                tmpl="Terminé." ;;
    fr:env_not_found_copy)  tmpl="Fichier .env introuvable — copie depuis amp/.env.example" ;;
    fr:env_edit_hint)       tmpl="Modifiez %s — définissez SERVER_IP et toutes les valeurs change_me_* — puis relancez." ;;
    fr:env_defaults)        tmpl="Fichier .env introuvable. Poursuite avec les valeurs par défaut." ;;
    fr:prereq_missing)      tmpl="'%s' n'est pas installé ou absent du PATH. Veuillez l'installer." ;;
    fr:dc_not_found)        tmpl="Ni le plugin 'docker compose' ni 'docker-compose' n'est disponible." ;;
    fr:unknown_arg)         tmpl="Argument inconnu : %s" ;;
    fr:bg_running)          tmpl="Exécution en arrière-plan (PID %s)" ;;
    fr:bg_log)              tmpl="Journal : %s" ;;
    fr:bg_follow)           tmpl="Suivre la progression : tail -f %s" ;;
    fr:unsupported_lang)    tmpl="Langue non supportée '%s' — retour à l'anglais (en)." ;;
    # ── English (default) ──────────────────────────────────────────────────────
    *:checking_prereqs)     tmpl="Checking prerequisites..." ;;
    *:creating_log_dirs)    tmpl="Creating host log directories..." ;;
    *:dir_created)          tmpl="  Created %s" ;;
    *:dir_exists)           tmpl="  Already exists: %s" ;;
    *:tb_cloning)           tmpl="Cloning TruBudget repository (%s)..." ;;
    *:tb_cloned)            tmpl="TruBudget repo already cloned at %s" ;;
    *:tb_update_hint)       tmpl="  To update: cd %s && git pull" ;;
    *:tb_writing_env)       tmpl="Writing TruBudget .env from our settings..." ;;
    *:tb_running_no_ver)    tmpl="TruBudget is already running — could not detect image version, skipping to preserve data." ;;
    *:tb_running_same)      tmpl="TruBudget is already running at version '%s' — skipping start to preserve data." ;;
    *:tb_ver_change)        tmpl="TruBudget version change detected: running='%s' → configured='%s'" ;;
    *:tb_pulling)           tmpl="Pulling new TruBudget images for running services: %s" ;;
    *:tb_restarting)        tmpl="Restarting TruBudget with new images (volumes preserved)..." ;;
    *:tb_upgraded)          tmpl="TruBudget upgraded to '%s'." ;;
    *:tb_starting_full)     tmpl="Starting TruBudget (full — all services)..." ;;
    *:tb_starting_slim)     tmpl="Starting TruBudget (slim: blockchain + api + frontend)..." ;;
    *:tb_started)           tmpl="TruBudget started." ;;
    *:tb_stopping)          tmpl="Stopping TruBudget containers..." ;;
    *:amp_image_missing)    tmpl="AMP_IMAGE and AMP_TAG must be set in %s" ;;
    *:amp_image_ci_hint)    tmpl="They are set by the GitHub Actions CI pipeline (build-push-ecr.yml)." ;;
    *:aws_creds_missing)    tmpl="AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY, and AWS_DEFAULT_REGION must be set in %s" ;;
    *:aws_installing)       tmpl="AWS CLI not found — installing..." ;;
    *:aws_installed)        tmpl="AWS CLI installed: %s" ;;
    *:ecr_auth)             tmpl="Authenticating with ECR (%s)..." ;;
    *:amp_pulling)          tmpl="Pulling AMP image: %s:%s" ;;
    *:skip_pull)            tmpl="Skipping image pull (--skip-build)" ;;
    *:pulling_postgis)      tmpl="Pulling postgres+postgis image..." ;;
    *:starting_amp)         tmpl="Starting AMP + PostgreSQL..." ;;
    *:waiting_amp)          tmpl="Waiting for AMP to be reachable..." ;;
    *:amp_up)               tmpl="AMP is up." ;;
    *:amp_timeout)          tmpl="AMP did not respond within %ss. Check logs:" ;;
    *:amp_logs_hint)        tmpl="  AMP logs:          %s" ;;
    *:tb_logs_hint)         tmpl="  TruBudget logs:    docker compose -f %s logs -f" ;;
    *:stack_running)        tmpl="Stack is running." ;;
    *:useful_commands)      tmpl="Useful commands:" ;;
    *:stop_hint)            tmpl="  Stop all:          ./setup.sh --down" ;;
    *:remove_hint)          tmpl="  Remove all data:   ./setup.sh --down-all" ;;
    *:tb_config_hint)       tmpl="After the stack is up, configure TruBudget in the AMP admin panel:" ;;
    *:stopping_amp)         tmpl="Stopping AMP containers..." ;;
    *:removing_volumes)     tmpl="Removing AMP volumes (data will be lost)!" ;;
    *:done)                 tmpl="Done." ;;
    *:env_not_found_copy)   tmpl=".env not found — copying from amp/.env.example" ;;
    *:env_edit_hint)        tmpl="Edit %s — set SERVER_IP and all change_me_* values — then re-run." ;;
    *:env_defaults)         tmpl=".env not found. Continuing with defaults." ;;
    *:prereq_missing)       tmpl="'%s' is not installed or not in PATH. Please install it first." ;;
    *:dc_not_found)         tmpl="Neither 'docker compose' plugin nor 'docker-compose' found." ;;
    *:unknown_arg)          tmpl="Unknown argument: %s" ;;
    *:bg_running)           tmpl="Running in background (PID %s)" ;;
    *:bg_log)               tmpl="Log: %s" ;;
    *:bg_follow)            tmpl="Follow progress: tail -f %s" ;;
    *:unsupported_lang)     tmpl="Unsupported language '%s' — falling back to English (en)." ;;
    *)                      tmpl="[?:${key}]" ;;
  esac
  # shellcheck disable=SC2059
  printf "${tmpl}" "$@"
}

# ── Paths ─────────────────────────────────────────────────────────────────────
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ENV_FILE="${SCRIPT_DIR}/.env"
COMPOSE_FILE="${SCRIPT_DIR}/docker-compose.yml"
TRUBUDGET_DIR="${SCRIPT_DIR}/trubudget"
TRUBUDGET_OP_DIR="${TRUBUDGET_DIR}/scripts/operation"

# ── Defaults ──────────────────────────────────────────────────────────────────
WITH_TRUBUDGET=true
FULL_TRUBUDGET=false
SKIP_BUILD=false
TEARDOWN=false
TEARDOWN_VOLUMES=false
BACKGROUND=false
SETUP_LANG="en"

# ── Argument parsing ──────────────────────────────────────────────────────────
for arg in "$@"; do
  case "$arg" in
    --no-trubudget)    WITH_TRUBUDGET=false ;;
    --full-trubudget)  FULL_TRUBUDGET=true ;;
    --skip-build)    SKIP_BUILD=true ;;
    --down)          TEARDOWN=true ;;
    --down-all)      TEARDOWN=true; TEARDOWN_VOLUMES=true ;;
    --background)    BACKGROUND=true ;;
    --lang=*)          SETUP_LANG="${arg#--lang=}" ;;
    --help|-h)
      sed -n '/^# Usage:/,/^[^#]/p' "$0" | grep '^#' | sed 's/^# //'
      exit 0
      ;;
    *)
      err "$(t unknown_arg "$arg")"
      exit 1
      ;;
  esac
done

# Validate language code — warn and fall back to English if unsupported
case "${SETUP_LANG}" in
  en|fr) ;;
  *)
    echo -e "${YELLOW}[warn]${NC}  $(t unsupported_lang "${SETUP_LANG}")"
    SETUP_LANG="en"
    ;;
esac

# ── Background mode ───────────────────────────────────────────────────────────
# Re-invoke this script without --background, detached from the terminal.
# All output goes to SETUP_LOG; the parent exits immediately.
if [[ "${BACKGROUND}" == true ]]; then
  SETUP_LOG="${SCRIPT_DIR}/setup-$(date +%Y%m%d-%H%M%S).log"
  # Rebuild the original argument list, minus --background
  FORWARD_ARGS=()
  for arg in "$@"; do
    [[ "${arg}" == "--background" ]] && continue
    FORWARD_ARGS+=("${arg}")
  done
  nohup bash "${BASH_SOURCE[0]}" "${FORWARD_ARGS[@]}" > "${SETUP_LOG}" 2>&1 &
  BG_PID=$!
  echo -e "${GREEN}[setup]${NC} $(t bg_running "${BG_PID}")"
  echo -e "${GREEN}[setup]${NC} $(t bg_log "${SETUP_LOG}")"
  echo -e "${GREEN}[setup]${NC} $(t bg_follow "${SETUP_LOG}")"
  exit 0
fi

# ── Prerequisite checks ───────────────────────────────────────────────────────
check_cmd() {
  if ! command -v "$1" &>/dev/null; then
    err "$(t prereq_missing "$1")"
    exit 1
  fi
}

log "$(t checking_prereqs)"
check_cmd docker
check_cmd git

if docker compose version &>/dev/null 2>&1; then
  DC="docker compose"
elif command -v docker-compose &>/dev/null; then
  DC="docker-compose"
else
  err "$(t dc_not_found)"
  exit 1
fi

# ── Env file ──────────────────────────────────────────────────────────────────
if [[ ! -f "${ENV_FILE}" ]]; then
  if [[ -f "${SCRIPT_DIR}/amp/.env.example" ]]; then
    warn "$(t env_not_found_copy)"
    cp "${SCRIPT_DIR}/amp/.env.example" "${ENV_FILE}"
    warn "$(t env_edit_hint "${ENV_FILE}")"
    exit 1
  else
    warn "$(t env_defaults)"
  fi
fi

# Load env so we can use vars in this script
# Strip inline comments before sourcing — bash executes them otherwise.
if [[ -f "${ENV_FILE}" ]]; then
  while IFS= read -r line; do
    [[ -z "${line}" || "${line}" =~ ^[[:space:]]*# ]] && continue
    line="${line%%  #*}"
    line="${line%% #*}"
    export "${line?}" 2>/dev/null || true
  done < <(grep -v '^[[:space:]]*#' "${ENV_FILE}" | grep '=')
fi

# ── Tear-down path ────────────────────────────────────────────────────────────
if [[ "${TEARDOWN}" == true ]]; then
  log "$(t stopping_amp)"
  if [[ "${TEARDOWN_VOLUMES}" == true ]]; then
    warn "$(t removing_volumes)"
    $DC -f "${COMPOSE_FILE}" --env-file "${ENV_FILE}" down -v --remove-orphans
  else
    $DC -f "${COMPOSE_FILE}" --env-file "${ENV_FILE}" down --remove-orphans
  fi

  if [[ -f "${TRUBUDGET_OP_DIR}/start-trubudget.sh" ]]; then
    log "$(t tb_stopping)"
    docker compose -f "${TRUBUDGET_OP_DIR}/docker-compose.yml" \
      -p trubudget-operation down --remove-orphans || true
  fi
  log "$(t done)"
  exit 0
fi

# ── Create host log directories ───────────────────────────────────────────────
LOG_DIRS=(/var/log/amp /var/log/trubudget-blockchain /var/log/trubudget-api /var/log/trubudget-frontend)
log "$(t creating_log_dirs)"
for dir in "${LOG_DIRS[@]}"; do
  if [[ ! -d "${dir}" ]]; then
    sudo mkdir -p "${dir}" && sudo chmod 755 "${dir}"
    log "$(t dir_created "${dir}")"
  else
    log "$(t dir_exists "${dir}")"
  fi
done

# ── TruBudget: clone official repo ───────────────────────────────────────────
if [[ "${WITH_TRUBUDGET}" == true ]]; then
  TRUBUDGET_REPO="${TRUBUDGET_REPO:-https://github.com/openkfw/TruBudget.git}"
  TRUBUDGET_TAG="${TRUBUDGET_TAG:-main}"

  if [[ ! -d "${TRUBUDGET_DIR}/.git" ]]; then
    log "$(t tb_cloning "${TRUBUDGET_TAG}")"
    git clone --depth 1 --branch "${TRUBUDGET_TAG}" "${TRUBUDGET_REPO}" "${TRUBUDGET_DIR}"
  else
    log "$(t tb_cloned "${TRUBUDGET_DIR}")"
    log "$(t tb_update_hint "${TRUBUDGET_DIR}")"
  fi

  # ── TruBudget: write their .env ───────────────────────────────────────────────────
  log "$(t tb_writing_env)"
  TRUBUDGET_ENV="${TRUBUDGET_OP_DIR}/.env"

  # Start from their example so all their internal flags are present
  cp "${TRUBUDGET_OP_DIR}/.env.example" "${TRUBUDGET_ENV}"

  # Strip inline comments — Docker Compose .env files do NOT support them.
  # TruBudget's .env.example has lines like: BETA_ENABLED=false # some comment
  # which makes the value "false # some comment" instead of "false", breaking validation.
  # This regex only acts on non-comment lines (those not starting with #) and strips
  # trailing " # ..." patterns. It preserves # inside values (e.g. secrets ending with #).
  sed -i -E '/^[^#]/s/[[:space:]]+#[[:space:]].+$//' "${TRUBUDGET_ENV}"

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
  # Fix cookie SameSite/Secure flags for plain HTTP (IP-only) access.
  # Without this, the JWT cookie set after login is silently dropped by browsers
  # because SameSite=None requires HTTPS, so every request after login gets 401.
  set_env "REACT_APP_API_SERVICE_ADDITIONAL_NGINX_CONF" "proxy_cookie_flags ~ nosecure samesite=lax;"

  # ── TruBudget: start / upgrade via official script ──────────────────────────
  # Strategy:
  #   - Not running            → call start-trubudget.sh (first deploy)
  #   - Running, same version  → skip entirely (preserves data, avoids restart)
  #   - Running, new version   → pull + restart ONLY the already-running services
  #                              (NO -v, so blockchain volumes are kept)
  #   - Running, can't detect  → skip (safe default)
  CONFIGURED_TAG="${TRUBUDGET_VERSION:-latest}"

  TRUBUDGET_RUNNING=$(docker compose -f "${TRUBUDGET_OP_DIR}/docker-compose.yml" \
    -p trubudget-operation ps --quiet --status running 2>/dev/null | wc -l | tr -d ' ')

  if [[ "${TRUBUDGET_RUNNING}" -gt 0 ]]; then
    # Discover the service names that are actually running (avoids hardcoding)
    SLIM_SERVICES=$(docker compose -f "${TRUBUDGET_OP_DIR}/docker-compose.yml" \
      -p trubudget-operation ps --services --status running 2>/dev/null | tr '\n' ' ' | xargs)

    # Get the image tag of the first running container (all slim services share the same TAG)
    FIRST_CONTAINER=$(docker compose -f "${TRUBUDGET_OP_DIR}/docker-compose.yml" \
      -p trubudget-operation ps -q 2>/dev/null | head -1)
    RUNNING_TAG=""
    if [[ -n "${FIRST_CONTAINER}" ]]; then
      RUNNING_TAG=$(docker inspect "${FIRST_CONTAINER}" \
        --format '{{.Config.Image}}' 2>/dev/null | awk -F: '{print $NF}' || echo "")
    fi

    if [[ -z "${RUNNING_TAG}" ]]; then
      log "$(t tb_running_no_ver)"
    elif [[ "${RUNNING_TAG}" == "${CONFIGURED_TAG}" ]]; then
      log "$(t tb_running_same "${CONFIGURED_TAG}")"
    else
      warn "$(t tb_ver_change "${RUNNING_TAG}" "${CONFIGURED_TAG}")"
      log "$(t tb_pulling "${SLIM_SERVICES}")"
      # shellcheck disable=SC2086
      docker compose -f "${TRUBUDGET_OP_DIR}/docker-compose.yml" \
        -p trubudget-operation pull ${SLIM_SERVICES}
      log "$(t tb_restarting)"
      # shellcheck disable=SC2086
      docker compose -f "${TRUBUDGET_OP_DIR}/docker-compose.yml" \
        -p trubudget-operation up -d --remove-orphans ${SLIM_SERVICES}
      log "$(t tb_upgraded "${CONFIGURED_TAG}")"
    fi
  else
    if [[ "${FULL_TRUBUDGET}" == true ]]; then
      log "$(t tb_starting_full)"
      bash "${TRUBUDGET_OP_DIR}/start-trubudget.sh" --no-log
    else
      log "$(t tb_starting_slim)"
      bash "${TRUBUDGET_OP_DIR}/start-trubudget.sh" --slim --no-log
    fi
    log "$(t tb_started)"
  fi
fi

# ── ECR login & pull AMP image ───────────────────────────────────────────────
if [[ -z "${AMP_IMAGE:-}" || -z "${AMP_TAG:-}" ]]; then
  err "$(t amp_image_missing "${ENV_FILE}")"
  err "$(t amp_image_ci_hint)"
  exit 1
fi

if [[ -z "${AWS_ACCESS_KEY_ID:-}" || -z "${AWS_SECRET_ACCESS_KEY:-}" || -z "${AWS_DEFAULT_REGION:-}" ]]; then
  err "$(t aws_creds_missing "${ENV_FILE}")"
  exit 1
fi

if [[ "${SKIP_BUILD}" == false ]]; then
  if ! command -v aws &>/dev/null; then
    log "$(t aws_installing)"
    curl -fsSL "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o /tmp/awscliv2.zip
    unzip -q /tmp/awscliv2.zip -d /tmp/awscliv2
    sudo /tmp/awscliv2/aws/install
    rm -rf /tmp/awscliv2.zip /tmp/awscliv2
    log "$(t aws_installed "$(aws --version)")"
  fi

  # Extract the ECR registry hostname (everything before the first /)
  ECR_REGISTRY=$(echo "${AMP_IMAGE}" | cut -d'/' -f1)

  log "$(t ecr_auth "${ECR_REGISTRY}")"
  aws ecr get-login-password --region "${AWS_DEFAULT_REGION}" \
    | docker login --username AWS --password-stdin "${ECR_REGISTRY}"

  log "$(t amp_pulling "${AMP_IMAGE}" "${AMP_TAG}")"
  docker pull "${AMP_IMAGE}:${AMP_TAG}"
else
  log "$(t skip_pull)"
fi

# ── Start AMP ─────────────────────────────────────────────────────────────────
# Pre-pull all images explicitly so docker compose up never needs to pull.
# The amp image was already pulled above; pull postgres here (public, no auth).
log "$(t pulling_postgis)"
docker pull postgis/postgis:14-3.4-alpine

# Authenticate with ECR (ensures daemon has fresh credentials)
if [[ -n "${AWS_ACCESS_KEY_ID:-}" ]]; then
  ECR_REGISTRY=$(echo "${AMP_IMAGE}" | cut -d'/' -f1)
  aws ecr get-login-password --region "${AWS_DEFAULT_REGION}" \
    | docker login --username AWS --password-stdin "${ECR_REGISTRY}" 2>/dev/null || true
fi

log "$(t starting_amp)"
# Use --pull never (Compose v2) or --no-pull (Compose v1) — all images already pulled above.
if [[ "${DC}" == "docker compose" ]]; then
  $DC -f "${COMPOSE_FILE}" --env-file "${ENV_FILE}" up -d --remove-orphans --pull never
else
  $DC -f "${COMPOSE_FILE}" --env-file "${ENV_FILE}" up -d --remove-orphans --no-pull
fi

# ── Wait for AMP ─────────────────────────────────────────────────────────────
log "$(t waiting_amp)"
AMP_PORT="${AMP_PORT:-8080}"
RETRIES=60
WAIT=5
for i in $(seq 1 $RETRIES); do
  if curl -sf "http://localhost:${AMP_PORT}/ping" &>/dev/null; then
    log "$(t amp_up)"
    break
  fi
  echo -ne "${CYAN}  attempt ${i}/${RETRIES} ...${NC}\r"
  sleep $WAIT
  if [[ $i -eq $RETRIES ]]; then
    warn "$(t amp_timeout "$((RETRIES * WAIT))")  "
    warn "  $DC -f ${COMPOSE_FILE} logs -f amp"
  fi
done

# ── Summary ───────────────────────────────────────────────────────────────────
SERVER_IP="${SERVER_IP:-<your-server-ip>}"
AMP_PORT="${AMP_PORT:-8080}"
TRUBUDGET_UI_PORT="${TRUBUDGET_UI_PORT:-3000}"
TRUBUDGET_API_PORT="${TRUBUDGET_API_PORT:-8081}"

echo ""
log "$(t stack_running)"
echo -e "  AMP:              ${CYAN}http://${SERVER_IP}:${AMP_PORT}${NC}"
if [[ "${WITH_TRUBUDGET}" == true ]]; then
  echo -e "  TruBudget UI:     ${CYAN}http://${SERVER_IP}:${TRUBUDGET_UI_PORT}${NC}"
  echo -e "  TruBudget API:    ${CYAN}http://${SERVER_IP}:${TRUBUDGET_API_PORT}/api${NC}"
fi
echo ""
log "$(t useful_commands)"
echo "$(t amp_logs_hint "$DC -f ${COMPOSE_FILE} logs -f amp")"
echo "$(t tb_logs_hint "${TRUBUDGET_OP_DIR}/docker-compose.yml")"
echo "$(t stop_hint)"
echo "$(t remove_hint)"
echo ""
log "$(t tb_config_hint)"
echo "  Admin > Global Settings > trubudget > baseUrl"
echo "  Value: http://host.docker.internal:${TRUBUDGET_API_PORT}/"
