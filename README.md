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

## Update DB 

### Re-generate a clean DB
```
$ kubectl exec -n foundation-internal-webdev-apps $(kubectl get -n foundation-internal-webdev-apps pod -l "app=projects-bots-api,environment=production" -o json | jq -r ".items[0]|.metadata.name") cat /deployments/bots.db.json | jq > bots.db.json
$ ./gen_bot_db.sh bots.db.json | tee bots.db.new.json
$ jq < bots.db.new.json > bots.db.new.json.formatted && mv bots.db.new.json.formatted bots.db.new.json
```

Check the differences between the currently deployed DB (`bots.db.json`) and the new one (`bots.db.new.json`) and merge in `bots.db.json` if required.

```
$ diff bots.db.json bots.db.new.json 
```

### Build a new docker image with the updated DB

Run a clean build of the source (if required)

```
$ mvn clean package # if not already build
```

Build and push the docker image.

```
$ docker build --pull --rm -t eclipsefdn/projects-bots-api:latest -f src/main/docker/Dockerfile .
$ docker push eclipsefdn/projects-bots-api:latest
```

### Deploy to cluster

(Requires kubectl client 1.15+)

```
$ kubectl rollout restart -n foundation-internal-webdev-apps deployment/projects-bots-api 
```

## Copyright 

Copyright (c) 2020 Eclipse Foundation and others.
This program and the accompanying materials are made available under the terms of the Eclipse Public License 2.0 which is available at http://www.eclipse.org/legal/epl-v20.html,

SPDX-License-Identifier: EPL-2.0
