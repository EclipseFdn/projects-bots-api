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

# check for errors
jq . bots.db.new.json

# run a clean build
mvn clean package -DskipTests

# build and push docker image
image_name="eclipsefdn/projects-bots-api:latest"
docker build --pull --rm -t "${image_name}" -f src/main/docker/Dockerfile .
docker push "${image_name}"

# deploy to cluster
kubectl rollout restart -n foundation-internal-webdev-apps deployment/projects-bots-api 