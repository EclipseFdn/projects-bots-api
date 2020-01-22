# Eclipse Foundation Bots API

## Build

* Development 

    $ mvn compile quarkus:dev
   
* Build and run test

    $ mvn clean package
    
* Build native 

    $ mvn package -Pnative
    
* Build native & docker image

    $ mvn package -Pnative -Dnative-image.docker-build=true
    
See https://quarkus.io for more information.  

## Deploy 

```
$ kubectl rsh -n foundation-internal-webdev-apps $(kubectl get pod -l "app=projects-bots-api,environment=production" -o json | jq -r ".items[0]|.metadata.name") cat /deployments/bots.db.json > bots.db.json
$ ./gen_bot_db.sh bots.db.new.json
$ diff bots.db.json bots.db.new.json
# If required, merge the two
$ docker build --rm -t eclipsefdn/projects-bots-api:latest -f src/main/docker/Dockerfile .
$ docker push eclipsefdn/projects-bots-api:latest
# Requires kubectl client 1.15+
$ kubectl rollout restart deployment/projects-bots-api 
```

## Copyright 

Copyright (c) 2019 Eclipse Foundation and others.
This program and the accompanying materials are made available under the terms of the Eclipse Public License 2.0 which is available at http://www.eclipse.org/legal/epl-v20.html,

SPDX-License-Identifier: EPL-2.0
