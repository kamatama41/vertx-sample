#!/bin/sh
# For remote debugging
export JAVA_OPTS='-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8000'
cd ..
./gradlew runMod -i