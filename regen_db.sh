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

kubectl exec -n foundation-internal-webdev-apps "$(kubectl get -n foundation-internal-webdev-apps pod -l "app=projects-bots-api,environment=production" -o json | jq -r ".items[0]|.metadata.name")" -- cat /deployments/bots.db.json | jq -S 'sort_by(.id)' > bots.db.json
./gen_bot_db.sh bots.db.json > bots.db.new.jsonnet
jsonnet src/main/jsonnet/extensions.jsonnet | jq -S 'sort_by(.id)' > bots.db.new.json
rm -f bots.db.new.jsonnet

printf "\nRun 'diff bots.db.json bots.db.new.json' before deploying first!!"