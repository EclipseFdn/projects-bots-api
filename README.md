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
$ ./regen_db.sh
```

Check the differences between the currently deployed DB (`bots.db.json`) and the new one (`bots.db.new.json`) and merge in `bots.db.json` if required.

```
$ diff bots.db.json bots.db.new.json 
```

### Deploy a new docker image with the updated DB

```
$ ./deploy_db.sh
```

### Adding an external Bot

Edit file `src/main/jsonnet/extensions.jsonnet` accordingly.

## Dependencies

* [bash 4](https://www.gnu.org/software/bash/)
* [curl](https://curl.se/)
* [docker](https://www.docker.com)
* [jq](https://stedolan.github.io/jq/)
* [jsonnet](https://jsonnet.org/)
* [kubectl](https://kubernetes.io/docs/tasks/tools/install-kubectl-linux/)
* [maven](https://maven.apache.org)
* parallel (sudo apt install parallel)
* [pass](https://www.passwordstore.org)

## Copyright 

Copyright (c) 2020 Eclipse Foundation and others.
This program and the accompanying materials are made available under the terms of the Eclipse Public License 2.0 which is available at http://www.eclipse.org/legal/epl-v20.html,

SPDX-License-Identifier: EPL-2.0
