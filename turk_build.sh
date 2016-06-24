#!/bin/bash

lein with-profile skybox-turk javac
lein with-profile skybox-turk jar
mv target/skybox-1.0-SNAPSHOT.jar target/skybox-turk-1.0-SNAPSHOT.jar
lein localrepo coords target/skybox-turk-1.0-SNAPSHOT.jar | xargs lein localrepo install
