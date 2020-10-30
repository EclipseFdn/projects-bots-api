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

kubectl exec -n foundation-internal-webdev-apps $(kubectl get -n foundation-internal-webdev-apps pod -l "app=projects-bots-api,environment=production" -o json | jq -r ".items[0]|.metadata.name") -- cat /deployments/bots.db.json | jq > bots.db.json
./gen_bot_db.sh bots.db.json | tee bots.db.new.json
jq < bots.db.new.json > bots.db.new.json.formatted && mv bots.db.new.json.formatted bots.db.new.json

printf "\nRun 'diff bots.db.json bots.db.new.json' before deploying first!!"