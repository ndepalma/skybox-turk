#!/bin/bash

lein jar
lein localrepo coords target/skybox-turk-1.0-SNAPSHOT.jar | xargs lein localrepo install
