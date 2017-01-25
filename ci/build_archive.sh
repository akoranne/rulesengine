#!/bin/bash

echo ""
echo " .. Running build"
echo $localEnv

if [ "$localEnv" == "development" ];then
	cd service-repo
else
#	cd service-repo-master
	cd service-repo
fi

# file="../version/number"
# version=$(cat "$file")
# echo $version

# gradle build
export GRADLE_OPTS="-Dorg.gradle.native=false"
./gradlew clean build

# create target folder
mkdir ../build-output

# move all manifests file to target
cp *-manifest.yml  ../build-output/

cp build/libs/*.jar ../build-output/
