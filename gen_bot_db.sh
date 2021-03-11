#!/usr/bin/env bash

# Bash strict-mode
set -o errexit
set -o nounset
set -o pipefail

export PASSWORD_STORE_DIR="${PASSWORD_STORE_DIR:-"${HOME}/.password-store-cbi"}"

SCRIPT_NAME="$(basename "${0}")"
INPUT_FILE="${1:-}"

usage() {
  printf "Usage: %s INPUT_FILE\n" "$SCRIPT_NAME"
  printf "\t%-16s input file.\n" "INPUT_FILE"
}

verify_inputs() {
  # check that input file exists
  if [ -z "${INPUT_FILE:-}" ]; then
    printf "ERROR: make sure an input file is given.\n"
    usage
    exit 1
  fi
  if [ ! -f "${INPUT_FILE:-}" ]; then
    printf "ERROR: '%s' is not a regular file or does not exist.\n" "${INPUT_FILE}"
    usage
    exit 1
  fi
}

request_access_token() {
  local client_id client_secret
  client_id="$(pass api.eclipse.org/client_id)"
  client_secret="$(pass api.eclipse.org/client_secret)"
  curl -sSLf --request POST \
    --url 'https://accounts.eclipse.org/oauth2/token' \
    --header 'content-type: application/x-www-form-urlencoded' \
    --data 'grant_type=client_credentials' \
    --data "client_id=${client_id}" \
    --data "client_secret=${client_secret}" \
    --data 'scope=eclipsefdn_view_all_profiles' | jq -r '.access_token'
}

query_email_by_user_id() {
    local user_id="$1"
    local user_profile
    user_profile="$(mktemp)"
    if curl -sLf --request GET \
      --retry 8 \
      --output "${user_profile}" \
      --url "https://api.eclipse.org/account/profile/${user_id}.json" \
      --header "Authorization: Bearer ${ACCESS_TOKEN}" >&2 ; then
      jq -r '.mail' <"${user_profile}"
    else
      >&2 echo "DEBUG: unable to retrieve user profile from 'https://api.eclipse.org/account/profile/${user_id}.json'"
    fi
    rm -f "${user_profile}"
}

passEntry() {
  local entry="${1}"
  
  local passContent
  passContent="$(mktemp)"
  
  local retry=0
  while ! pass "${entry}" > "${passContent}" 2>/dev/null && [[ ${retry} -lt 8 ]]; do
    retry=$((retry++))
  done

  if [[ -s "${passContent}" ]]; then
    cat "${passContent}"
  else 
    >&2 echo "ERROR: unable to retrieve pass entry '${entry}'"
  fi
  
  rm -f "${passContent}"
}

printSiteIdentityJson() {
  local projectId="${1}"
  local siteName="${2}"
  local pathPrefix="bots/${projectId}/${siteName}"
  if [[ -d "${PASSWORD_STORE_DIR}/${pathPrefix}" ]]; then
    printf '"%s": {' "${siteName}"
    if [[ -f "${PASSWORD_STORE_DIR}/${pathPrefix}/username.gpg" ]]; then
      jsonKV "username" "$(passEntry "${pathPrefix}/username")"
    fi
    if [[ -f "${PASSWORD_STORE_DIR}/${pathPrefix}/email.gpg" ]]; then
      jsonKV "email" "$(passEntry "${pathPrefix}/email")"
    fi
    printf "},"
  fi
}

jsonKVInt() {
  printf '"%s": %s,' "${1}" "${2}"
}

jsonKV() {
  printf '"%s": "%s",' "${1}" "${2}"
}

printProjectJson() {
  local id="${1}"
  local projectId="${2}"
  local projectShortName="${projectId##*.}"

  printf "{"

  jsonKVInt "id" "${id}"
  jsonKV "projectId" "${projectId}"
  jsonKV "username" "genie.${projectShortName}"

  local ldap_email
  ldap_email="$(query_email_by_user_id "genie.${projectShortName}" | tr -d "[:space:]")"
  if [[ -n "${ldap_email:-}" ]]; then
    jsonKV "email" "${ldap_email}"
  else
    >&2 echo "WARNING: No LDAP email found for project '${projectId}'."
  fi

  printSiteIdentityJson "${projectId}" "github.com"
  printSiteIdentityJson "${projectId}" "gitlab.eclipse.org"
  printSiteIdentityJson "${projectId}" "oss.sonatype.org"
  printSiteIdentityJson "${projectId}" "docker.com"

  printf "}"
}

regenFromRemoteData() {
  local inputFile="${1}"
  local botId="${2}"
  local projectId
  projectId=$(jq -r '.[]|select(.id=='"${botId}"').projectId' < "${inputFile}")
  local projectPath="${PASSWORD_STORE_DIR}/bots/${projectId:-undefined}"
  if [ -d "${projectPath}" ]; then
    >&2 echo "INFO: Re-generating json for bot ${botId}:${projectId}"
    printProjectJson "${botId}" "${projectId}"
    printf ",\n"
  else
    >&2 echo "WARNING: Bot does not exist in pass ${botId}:${projectPath}"
  fi
}

verify_inputs
ACCESS_TOKEN="$(request_access_token)"

echo "["

export ACCESS_TOKEN
export -f regenFromRemoteData printProjectJson printSiteIdentityJson passEntry jsonKV jsonKVInt query_email_by_user_id
# shellcheck disable=SC2094
jq -r '.[]|.id' < "${INPUT_FILE}" | \
  SHELL=$(type -p bash) parallel --no-notice -j200% regenFromRemoteData "${INPUT_FILE}"

botId=$(jq -r '[.[]|.id]|max' < "${INPUT_FILE}")
for projectPath in "${PASSWORD_STORE_DIR}/bots"/*; do
  projectId="${projectPath##*/}"
  if [ -z "$(jq -r '.[]|select(.projectId=="'"${projectId}"'")' < "${INPUT_FILE}")" ]; then
    botId=$((botId+1))
    >&2 echo "INFO: New project detected. Adding it to DB with bot ${botId}:${projectId}"
    printProjectJson "${botId}" "${projectId}"
    printf ",\n"
  else 
    >&2 echo "DEBUG: No need to create new entry for project already in DB: ${projectId}"
  fi 
done

echo "]"

>&2 echo "INFO: Re-generated all bots!"

