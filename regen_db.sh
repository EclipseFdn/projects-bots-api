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
kubectl exec -n foundation-internal-webdev-apps "$(kubectl get -n foundation-internal-webdev-apps pod -l "app=projects-bots-api,environment=production" -o json | jq -r ".items[0]|.metadata.name")" -- cat /deployments/bots.db.json | jq -S 'sort_by(.id)' > "${OLD_JSON}"
./gen_bot_db.sh "${OLD_JSON}" > bots.db.new.jsonnet
jsonnet src/main/jsonnet/extensions.jsonnet | jq -S 'sort_by(.id)' > "${NEW_JSON}"
rm -f bots.db.new.jsonnet

diff "${OLD_JSON}" "${NEW_JSON}" || true

printf "\nRun 'diff %s %s' before deploying first!!\n" "${OLD_JSON}" "${NEW_JSON}"
popd