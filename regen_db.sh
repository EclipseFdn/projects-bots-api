#!/usr/bin/env bash
#*******************************************************************************
# Copyright (c) 2020 Eclipse Foundation and others.
# This program and the accompanying materials are made available
# under the terms of the Eclipse Public License 2.0
# which is available at http://www.eclipse.org/legal/epl-v20.html
# SPDX-License-Identifier: EPL-2.0
#*******************************************************************************

# Bash strict-mode
set -o errexit
set -o nounset
set -o pipefail

SCRIPT_FOLDER="$(dirname "$(readlink -f "${BASH_SOURCE[0]}")")"

OLD_JSON="bots.db.old.json"
NEW_JSON="bots.db.json"

pushd "${SCRIPT_FOLDER}"

echo "* Pulling latest version of projects-bots-api..."
git pull

#TODO: don't update if the bot has been added before
cp "${NEW_JSON}" "${OLD_JSON}"
./gen_bot_db.sh "${OLD_JSON}" > bots.db.new.jsonnet
jsonnet src/main/jsonnet/extensions.jsonnet | jq -S 'sort_by(.id)' > "${NEW_JSON}"
rm -f bots.db.new.jsonnet

diff "${OLD_JSON}" "${NEW_JSON}" || true

#Show error if files are equal
if diff "${OLD_JSON}" "${NEW_JSON}" > /dev/null; then
  printf "\n\n"
  echo "ERROR: diff found no difference. Please double check if something is missing!"
  exit 1
fi

printf "\nRun 'diff %s %s' before deploying first!!\n" "${OLD_JSON}" "${NEW_JSON}"

printf "\n\n"

read -rsp $'Once you are done with comparing the diff, press any key to continue...\n' -n1

echo "* Committing changes to projects-bots-api repo..."
git add bots.db.json
git commit -m "Update bots.db.json"
git push

echo "* Commit should trigger a build of https://foundation.eclipse.org/ci/webdev/job/projects-bots-api/job/master..."
echo
echo "* TODO: Wait for the build to finish..."
printf "* TODO: Double check that bot account has been added to API (https://api.eclipse.org/bots)...\n"
read -rsp $'Once you are done, press any key to continue...\n' -n1

popd