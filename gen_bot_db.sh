#!/usr/bin/env bash

# Bash strict-mode
set -o errexit
set -o nounset
set -o pipefail

export PASSWORD_STORE_DIR=~/.password-store-cbi

script_name="$(basename ${0})"
input_file="${1:-}"

usage() {
  printf "Usage: %s input_file\n" "$script_name"
  printf "\t%-16s input file.\n" "input_file"
}

verify_inputs() {
  # check that input file exists
  if [ "${input_file}" == "" ]; then
    printf "ERROR: make sure an input file is given.\n"
    usage
    exit 1
  fi
  if [ ! -f "${input_file}" ]; then
    printf "ERROR: make sure the input file is accessible.\n"
    usage
    exit 1
  fi
}

request_access_token() {
  client_id=$(pass api.eclipse.org/client_id)
  client_secret=$(pass api.eclipse.org/client_secret)
  access_token=$(curl -s --request POST \
    --url 'https://accounts.eclipse.org/oauth2/token' \
    --header 'content-type: application/x-www-form-urlencoded' \
    --data 'grant_type=client_credentials' \
    --data "client_id=${client_id}" \
    --data "client_secret=${client_secret}" \
    --data 'scope=eclipsefdn_view_all_profiles' | jq -r '.access_token')
}

query_email_by_user_id() {
    local user_id="$1"
    email_address=$(curl -s --request GET \
      --url "https://api.eclipse.org/account/profile/${user_id}.json" \
      --header "Authorization: Bearer ${access_token}" | jq -r '.mail')
    echo ${email_address}
}

printProjectJson() {
  local id="${1}"
  local projectPath="${2}"
  local projectId="${projectPath##*/}"
  local projectShortName="${projectId##*.}"

  echo "{"

  echo '"id": '"${id}"','
  echo '"projectId": "'"${projectId}"'",'
  echo '"username": "genie.'"${projectShortName}"'"'

  ldap_email=$(query_email_by_user_id "genie.${projectShortName}")

  if [[ "${ldap_email}" != "" ]]; then
    echo ',"email": "'${ldap_email}'"'
  else
    >&2 echo "WARNING: No LDAP email found."
  fi

  if [[ -d "${projectPath}/github.com" ]] || [[ -d "${projectPath}/gitlab.eclipse.org" ]] || [[ -d "${projectPath}/oss.sonatype.org" ]] || [[ -d "${projectPath}/docker.com" ]]; then
    echo ","
  fi

  if [[ -d "${projectPath}/github.com" ]]; then
    echo '"github.com": {'
    echo '"username": "'$(pass bots/${projectId}/github.com/username)'",'
    echo '"email": "'$(pass bots/${projectId}/github.com/email)'"'
    if [[ -d "${projectPath}/gitlab.eclipse.org" ]] || [[ -d "${projectPath}/oss.sonatype.org" ]] || [[ -d "${projectPath}/docker.com" ]]; then
      echo "},"
    else
      echo "}"
   fi
  fi

  if [[ -d "${projectPath}/gitlab.eclipse.org" ]]; then
    echo '"gitlab.eclipse.org": {'
    echo '"username": "'$(pass bots/${projectId}/gitlab.eclipse.org/username)'",'
    echo '"email": "'$(pass bots/${projectId}/gitlab.eclipse.org/email)'"'
    if [[ -d "${projectPath}/oss.sonatype.org" ]] || [[ -d "${projectPath}/docker.com" ]]; then
      echo "},"
    else
      echo "}"
    fi
  fi

  if [[ -d "${projectPath}/oss.sonatype.org" ]]; then
    echo '"oss.sonatype.org": {'
    echo '"username": "'$(pass bots/${projectId}/oss.sonatype.org/username)'",'
    echo '"email": "'$(pass bots/${projectId}/oss.sonatype.org/email)'"'
    
    if [[ -d "${projectPath}/docker.com" ]]; then
      echo "},"
    else
      echo "}"
    fi
  fi

  if [[ -d "${projectPath}/docker.com" ]]; then
    echo '"docker.com": {'
    echo '"username": "'$(pass bots/${projectId}/docker.com/username)'",'
    echo '"email": "'$(pass bots/${projectId}/docker.com/email)'"'
    echo "}"
  fi

  echo "}"
}

verify_inputs
request_access_token

echo "["

botCount=0
for botId in $(jq -r '.[]|.id' < "${input_file}"); do
  projectId=$(jq -r '.[]|select(.id=='"${botId}"').projectId' < "${input_file}")
  projectPath="${PASSWORD_STORE_DIR}/bots/${projectId:-undefined}"
  if [ -d "${projectPath}" ]; then
    >&2 echo "Re-generating json for bot id ${botId} from ${projectPath} in 'pass'"
    if [[ ${botCount} -gt 0 ]]; then
      echo ","
    fi
    printProjectJson "${botId}" "${projectPath}" | jq -M
    botCount=$((botCount+1))
  else
    >&2 echo "Bot id ${botId} does not exist anymore at ${projectPath} in 'pass'"
  fi
done

botId=$(jq -r '[.[]|.id]|max' < "${input_file}")
for projectPath in "${PASSWORD_STORE_DIR}/bots"/*; do
  if [ -z "$(jq -r '.[]|select(.projectId=="'"${projectPath##*/}"'")' < "${input_file}")" ]; then
    botId=$((botId+1))
    >&2 echo "New project id ${projectPath##*/} detected. Adding it to DB with bot id ${botId}"
    if [[ ${botCount} -gt 0 ]]; then
      echo ","
    fi
    printProjectJson "${botId}" "${projectPath}" | jq -M
    botCount=$((botCount+1))
  else 
    >&2 echo "Project id ${projectPath##*/} is already in DB, no need to create new entry for it."
  fi 
done

echo "]"

>&2 echo "Re-generated ${botCount} bots!"

