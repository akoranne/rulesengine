#!/bin/bash

set -e # fail fast
#	set -x # print commands

echo ""
echo " .. Running build"
echo ""

cd service-repo

# gradle build
export GRADLE_OPTS="-Dorg.gradle.native=false"
./gradlew clean test assemble

# create target folder
# mkdir -f ../build-output

# move all manifests file to target
cp manifest.yml  ../build-output/

cp build/libs/*.jar ../build-output/

echo ""
echo " Build completed!!!"
echo ""
