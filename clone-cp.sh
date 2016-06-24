#!/bin/bash

#go to directory that I'm symlinking
pushd .
mkdir -p lib/lein-deps-clone/
cd lib/lein-deps-clone/
#clean it
rm *

#sym links
for i in $( lein with-profile +skybox-turk classpath | tr ':' '\n' | grep '\.m2' ); do
    ln -s $i
done

#pop back to orig dir
popd
