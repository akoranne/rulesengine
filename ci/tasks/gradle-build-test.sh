#!/bin/sh

set -e # fail fast
set -x # print commands

# The src from the 'develop' branch, injected by concourse
srcdir=`pwd`/source-code-develop

# change to the code repo (injected)
cd code-repo

gradle -v

# do a clean build using gradle, and execute all tests.
./gradlew clean test;


# if everything succeeds, assemble the archive (will be in the build/libs)
#		./gradlew assemble;

